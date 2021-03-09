package com.essue.jabac.client.policy;

import com.essue.jabac.client.Context;
import com.essue.jabac.core.policy.Policy;

import java.util.Collection;

/**
 * Responsible for loading polices for the current context/request. For example, you can load your
 * tenant specific policies by implementing this class
 */
public interface PolicyLoader {

  Collection<Policy> loadPolicies(Context context);
}
