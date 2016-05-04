package org.apache.james.mailbox.caching;

import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.exception.MailboxException;
import org.apache.james.mailbox.exception.SubscriptionException;
import org.apache.james.mailbox.store.MailboxSessionMapperFactory;
import org.apache.james.mailbox.store.mail.MailboxMapper;
import org.apache.james.mailbox.store.mail.MessageMapper;
import org.apache.james.mailbox.store.user.SubscriptionMapper;

/**
 * A MailboxSessionMapperFactory that uses the underlying MailboxSessionMapperFactory to provide
 * caching variants of MessageMapper and MailboxMapper built around the MessageMapper and MailboxMapper
 * provided by it
 * 
 */
public class CachingMailboxSessionMapperFactory extends
		MailboxSessionMapperFactory {

	private final MailboxSessionMapperFactory underlying;
	private final MailboxByPathCache mailboxByPathCache;
	private final MailboxMetadataCache mailboxMetadataCache;

	public CachingMailboxSessionMapperFactory(MailboxSessionMapperFactory underlying, MailboxByPathCache mailboxByPathCache, MailboxMetadataCache mailboxMetadataCache) {
		this.underlying = underlying;
		this.mailboxByPathCache = mailboxByPathCache;
		this.mailboxMetadataCache = mailboxMetadataCache;
	}
	
	@Override
	public MessageMapper createMessageMapper(MailboxSession session)
			throws MailboxException {
		return new CachingMessageMapper(underlying.createMessageMapper(session), mailboxMetadataCache);
	}

	@Override
	public MailboxMapper createMailboxMapper(MailboxSession session)
			throws MailboxException {
		return new CachingMailboxMapper(underlying.createMailboxMapper(session), mailboxByPathCache);
	}

	@Override
	public SubscriptionMapper createSubscriptionMapper(MailboxSession session)
			throws SubscriptionException {
		return underlying.createSubscriptionMapper(session);
	}

}
