package ru.iia.fartman.site.services;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
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
@Service
@Scope("singleton")
public class QueueUrls {
	/**
	 * Set of URLs in queue or polled from queue, need for unique
	 */
	private Set<Link> linkSet;

}
