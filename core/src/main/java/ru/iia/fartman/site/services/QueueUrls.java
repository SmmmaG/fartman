package ru.iia.fartman.site.services;

import ru.iia.fartman.orm.entity.Link;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Representation of unique URL queue
 */
public class QueueUrls {
	/**
	 * List of threads worked with this urls
	 */
	private List<String> workingThreadsList;
	/**
	 * Set of URLs in queue or polled from queue, need for unique
	 */
	private Set<Link> linkSet;
	/**
	 * Queue of URLs for Threads
	 */
	private Queue<Link> queue = new ConcurrentLinkedQueue<>();
	private List<PageReader> waitingThreadsList;

	public QueueUrls() {
		workingThreadsList = Collections.synchronizedList(new ArrayList<>());
		waitingThreadsList = Collections.synchronizedList(new ArrayList<>());

		linkSet = Collections.synchronizedSet(new HashSet<>());
	}

	public Link getNextUrl() {
		while (isWorking("")) {
			if (!queue.isEmpty()) {
				synchronized (queue) {
					return queue.poll();
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {

			}

		}
		return null;
	}

	public synchronized boolean addUrl(Link url) {
		if (linkSet.contains(url)) {
			return false;
		} else {
			linkSet.add(url);
			queue.add(url);
			return true;
		}
	}

	public void startWorkThread(PageReader threadName) {
		synchronized (waitingThreadsList) {
			waitingThreadsList.add(threadName);
		}
	}

	public void startThread(String threadName) {
		synchronized (workingThreadsList) {
			workingThreadsList.add(threadName);
		}
	}

	public boolean endThread(String threadName) {
		synchronized (workingThreadsList) {

			return workingThreadsList.remove(threadName);
		}
	}

	public synchronized boolean isWorking(String name) {
		for (PageReader reader : waitingThreadsList) {
			if ((!reader.getThreadName().equals(name)) && reader.isWorking()) {
				return true;
			}
		}
		return false;
	}

	public boolean isEmptyThreads() {
		return waitingThreadsList.isEmpty() || waitingThreadsList.size() == 1;
	}

}
