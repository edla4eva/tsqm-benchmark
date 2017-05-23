package com.tsqm.plugin.preferences;

import java.util.StringTokenizer;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.*;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import com.tsqm.plugin.TSQMPluginActivator;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class TSQMPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public TSQMPreferencePage() {
		super(GRID);
		setPreferenceStore(TSQMPluginActivator.getDefault().getPreferenceStore());
		setDescription("TSQM Preferences");
		}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		addField(new FileFieldEditor(PreferenceConstants.P_PATH, 
				"&File name preference (Metric Tool):", getFieldEditorParent()));
		addField(
			new BooleanFieldEditor(
				PreferenceConstants.P_BOOLEAN,
				"&Enable Benchmarker",
				getFieldEditorParent()));

		addField(new RadioGroupFieldEditor(
				PreferenceConstants.P_CHOICE,
			"&Select Mode",
			1,
			new String[][] { { "&Internal Metric Tools (PDepend)", "choice1" }, {
				"&External Metric Tools (PHP Metrics)", "choice2" }
		}, getFieldEditorParent()));
		addField(
			new StringFieldEditor(PreferenceConstants.P_STRING, "A &text preference:", getFieldEditorParent()));
		addField(
				new StringFieldEditor(PreferenceConstants.P_STRING2, "Another &text preference:", getFieldEditorParent()));
		//addField(
				//new StringFieldEditor(PreferenceConstants.P_STRING_METRICS, "&Metrics preference:", getFieldEditorParent()));
		//addField(
				//new StringFieldEditor(PreferenceConstants.P_STRING_METRICS_DESCR, "&Metrics Descriptions preference:", getFieldEditorParent()));
		//The last two fields will be invisible in production mode
		
		addField(
				new ListEditor(PreferenceConstants.P_STRING_METRICS_DESCR, "Add Metric descriptions:", getFieldEditorParent()) {
			@Override
			protected String createList(String[] items) {
				StringBuffer b = new StringBuffer();
				for (String item : items) {
					b.append(item).append(",");
				}
				return b.substring(0, b.length() - 1);
			}

			@Override
			protected String getNewInputObject() {
				InputDialog input = new InputDialog(getShell(), "New Metrics Description", "Please type a New Metrics Description", "", null);
				input.open();
				if (input.getReturnCode() == Window.OK) {
					return input.getValue();
				} /* else { */
				return null;
				/* } */
			}

			@Override
			protected String[] parseString(String stringList) {
				StringTokenizer t = new StringTokenizer(stringList, ",");
				int length = t.countTokens();
				String[] items = new String[length];
				for (int i = 0; i < length; i++) {
					items[i] = t.nextToken().trim();
				}
				return items;
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}