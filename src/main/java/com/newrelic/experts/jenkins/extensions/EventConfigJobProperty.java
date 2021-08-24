/*
 * Copyright 2019 New Relic Corporation. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0 
 */

package com.newrelic.experts.jenkins.extensions;

import com.google.inject.Inject;

import com.newrelic.experts.jenkins.events.EventHelper;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.JobProperty;

import jenkins.model.OptionalJobProperty;

import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import java.util.List;
import javax.annotation.CheckForNull;

/**
 * Definition of an optional {@link JobProperty} for New Relic configuration.
 *
 * @author Scott DeWitt (sdewitt@newrelic.com)
 */
@ExportedBean
public class EventConfigJobProperty extends OptionalJobProperty<Job<?,?>> {

  private boolean disableAppBuildEvents;
  private List<KeyValuePair> customAttributes;
  
  @DataBoundConstructor
  public EventConfigJobProperty() {}

  @Exported
  public boolean isDisableAppBuildEvents() {
    return disableAppBuildEvents;
  }

  @DataBoundSetter
  public void setDisableAppBuildEvents(boolean disableAppBuildEvents) {
    this.disableAppBuildEvents = disableAppBuildEvents;
  }

  @Exported
  public List<KeyValuePair> getCustomAttributes() {
    return this.customAttributes;
  }

  @DataBoundSetter
  public void setCustomAttributes(@CheckForNull List<KeyValuePair> customAttributes) {
    this.customAttributes = customAttributes;
  }

  @Extension(ordinal = -1000)
  @Symbol("newRelic")
  public static final class DescriptorImpl extends OptionalJobPropertyDescriptor {
    
    private EventHelper eventHelper;

    @Inject
    public void setEventHelper(EventHelper eventHelper) {
      this.eventHelper = eventHelper;
    }
    
    @Override
    public boolean isApplicable(@SuppressWarnings("rawtypes") Class<? extends Job> jobType) {
      return true;
    }

    @Override
    public String getDisplayName() {
      return "Customize New Relic build event settings";
    }
    
    public List<? extends Descriptor<KeyValuePair>> getCustomAttributesDescriptors() {
      return this.eventHelper.getJenkins().getDescriptorList(KeyValuePair.class);
    }
  }
}
  