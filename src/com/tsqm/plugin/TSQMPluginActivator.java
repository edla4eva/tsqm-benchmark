package com.tsqm.plugin;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;


//import net.sourceforge.metrics.core.MetricDescriptor;
//import net.sourceforge.metrics.core.MetricsPlugin;
import com.tsqm.core.Metric;

import net.sourceforge.metrics.core.MetricDescriptor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class TSQMPluginActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.tsqm.plugin"; //$NON-NLS-1$

	// The shared instance
	private static TSQMPluginActivator plugin;
	
	private String[] descriptions;
	private String[] ids;

	// Resource bundle.
	private ResourceBundle resourceBundle;

	private Map<String, MetricDescriptor> metrics = new LinkedHashMap<String, MetricDescriptor>();

	/**
	 * The constructor
	 */
	public TSQMPluginActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static TSQMPluginActivator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	/**
	 * get a list of all installed metric ids
	 * 
	 * @return String[]
	 */
	public String[] getMetricIds() {
		if (ids == null) {
			ids = parsePreferenceString(false);
		}
		return ids;
	}

	/**
	 * get a list of all installed metric descriptions
	 * 
	 * @return String[]
	 */
	public String[] getMetricDescriptions() {
		if (descriptions == null) {
			descriptions = parsePreferenceString(true);
		}
		return descriptions;
	}

	public MetricDescriptor getMetricDescriptor(String id) {
		return metrics.get(id);
	}

	/* Component:
	 * 
	 */
	private String[] parsePreferenceString(boolean description) {
		String stringList = getPreferenceStore().getString("METRICS.displayOrder");
		StringTokenizer t = new StringTokenizer(stringList, ",");
		int length = t.countTokens();
		String[] items = new String[length];
		for (int i = 0; i < length; i++) {
			String next = t.nextToken();
			int dash = next.indexOf('-');
			if (description) {
				items[i] = next.substring(dash + 1).trim();
			} else {
				items[i] = next.substring(0, dash).trim();
			}
		}
		return items;
	}
}
