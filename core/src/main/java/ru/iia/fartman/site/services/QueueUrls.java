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

	public QueueUrls() {
		workingThreadsList = Collections.synchronizedList(new ArrayList<>());
		linkSet = Collections.synchronizedSet(new HashSet<>());
	}

	public Link getNextUrl() {
		while (isWorking()) {
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

	public synchronized void startThread(String threadName) {
		workingThreadsList.add(threadName);
	}

	public synchronized boolean endThread(String threadName) {
		return workingThreadsList.remove(threadName);
	}

	public boolean isWorking() {
		return !workingThreadsList.isEmpty();
	}

}
