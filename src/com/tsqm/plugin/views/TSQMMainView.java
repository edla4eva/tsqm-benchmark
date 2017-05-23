package com.tsqm.plugin.views;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.events.ArmListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;

//additional imports
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;

import com.tsqm.ui.*;

//the big one
import net.sourceforge.metrics.calculators.*;
import net.sourceforge.metrics.ui.DependencyGraphView;
import net.sourceforge.metrics.ui.MetricsTable;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;

import swing2swt.layout.FlowLayout;
import swing2swt.layout.BorderLayout;

import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.custom.StackLayout;

import swing2swt.layout.BoxLayout;

import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

/**
 * This class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class TSQMMainView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.tsqm.plugin.views.TSQMMainView";
	
	/**
	 * The Information page of TSQM
	 */
	private final static String[] INFO = { "TSQM says No metrics available for selection. " +
			"To calculate and display metrics:", "" 
			};

	/**
	 * The description page for each TSQM View
	 */
	public final static String[] DESCRIPTION_1 ={

		"Shows the Main TSQM View", "",
		"This view enables the benchmarker to do the following", "",
		"	(1) Select metrics to benchmark", "",
		"	(2) Coordinate the benchmarking process", ""
	};
	
	public final static String[] DESCRIPTION_2 ={

		"Shows the Main Scource Code Analysis Unit (SCAU) View", "",
		"This view enables the benchmarker to do the following", "",
		"	(1) Select web-based source codes to use for benchmarking", "",
		"	(2) Pre-process the source codes", "",
		"	(3) Analyse the source code", ""
	};
	
	public final static String[] DESCRIPTION_3 ={

		"Shows the Metrics View", "",
		"This view enables the benchmarker to do the following", "",
		"	(1) See a list of all metrics to be benchmarked", "",
		"	(2) Save the metrics values...", "",
		"	(3) Perform some basic configurations", ""
	};
	
	protected final static String EOL = System.getProperty("line.separator");
	protected final static String EMPTY_STRING = "";
	private static ArmListener armListener;
	
	private TableViewer mainTableViewer;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;
	private Action selectionAction;
	private Label myLabel;
	Label lblDescription;
	private static Text textStatus;

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	class ViewContentProvider implements IStructuredContentProvider {
		@Override
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		@Override
		public void dispose() {
		}
		@Override
		public Object[] getElements(Object parent) {
			return new String[] { "Open SCAU View", "Open Benchmarking View", "Open Metrics View" };
		}
	}
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}
		@Override
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}
		@Override
		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().
					getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}
	class NameSorter extends ViewerSorter {
	}
	

	
	/**
	 * The constructor.
	 */
	public TSQMMainView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the mainTableViewer and initialize it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FormLayout());
		mainTableViewer = new TableViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		Table table = mainTableViewer.getTable();
		FormData fd_table = new FormData();
		fd_table.right = new FormAttachment(100, -10);
		fd_table.left = new FormAttachment(0, 2);
		fd_table.bottom = new FormAttachment(0, 215);
		fd_table.top = new FormAttachment(0, 10);
		table.setLayoutData(fd_table);
		
		textStatus = new Text(composite, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		FormData fd_textStatus = new FormData();
		fd_textStatus.right = new FormAttachment(table, 0, SWT.RIGHT);
		fd_textStatus.bottom = new FormAttachment(table, 225, SWT.BOTTOM);
		fd_textStatus.top = new FormAttachment(table, 20);
		fd_textStatus.left = new FormAttachment(0, 10);
		textStatus.setLayoutData(fd_textStatus);
		
		Composite composite_1 = new Composite(parent, SWT.NONE);
		composite_1.setLayout(new BorderLayout(0, 0));
		
		Label lblDescriptionHeader = new Label(composite_1, SWT.BORDER | SWT.WRAP);
		lblDescriptionHeader.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblDescriptionHeader.setBackground(SWTResourceManager.getColor(245, 245, 245));
		lblDescriptionHeader.setLayoutData(BorderLayout.NORTH);
		lblDescriptionHeader.setText("Description of selected item");
		
		lblDescription = new Label(composite_1, SWT.BORDER | SWT.WRAP);
		lblDescription.setLayoutData(BorderLayout.CENTER);
		lblDescription.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblDescription.setBackground(SWTResourceManager.getColor(SWT.COLOR_INFO_BACKGROUND));
		lblDescription.setText("\r\nShows the Main TSQM View\r\nThis view enables the benchmarker to do the following\r\n(1) Select metrics to benchmark\r\n(2) Coordinate the benchmarking process");
		
		Label lblDescriptionFooter = new Label(composite_1, SWT.BORDER | SWT.WRAP);
		lblDescriptionFooter.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		lblDescriptionFooter.setText("Developed by:\r\n Edoghogho OLAYE (edoghogho.olaye@uniben.edu)");
	
		lblDescriptionFooter.setLayoutData(BorderLayout.SOUTH);
		mainTableViewer.setContentProvider(new ViewContentProvider());
		mainTableViewer.setLabelProvider(new ViewLabelProvider());
		mainTableViewer.setSorter(new NameSorter());
		mainTableViewer.setInput(getViewSite());
		
		//Additional TSQM UI
		/*UIforTSQM newUI = new UIforTSQM();
		Display display = new Display();
		Shell objshlTsqmBencmarkTool = display.getActiveShell();
		UIforTSQM.main(new String[0]);
		 */
		// End TSQM UI

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(mainTableViewer.getControl(), "com.tsqm.plugin.viewer");
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		hookSelectionAction();
		contributeToActionBars();
	}

	

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				TSQMMainView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(mainTableViewer.getControl());
		mainTableViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, mainTableViewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
	}

	private void makeActions() {
		action1 = new Action() {
			@Override
			public void run() {
				showStatusMessage("View SCAU Action will be executed");
				displaySCAUView();
			}
		};
		action1.setText("View SCAU Action");
		action1.setToolTipText("View SCAU tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		
		action2 = new Action() {
			@Override
			public void run() {
				showStatusMessage("View TSQM Action will be executed");
				displayTSQM();
			}
		};
		action2.setText("View TSQM Action");
		action2.setToolTipText("View TSQM tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			@Override
			public void run() {
				ISelection selection = mainTableViewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				
				//
				showStatusMessage("Double-click detected on "+obj.toString());
				if (obj.toString()!="") displayTSQM();
				if (obj.toString()=="Open SCAU View") displaySCAUView();
				if (obj.toString()=="Open Benchmarking View") displayTSQM();
				if (obj.toString()=="Open Metrics View") displayMetricsView();
				
				
				
			}
		};
		selectionAction = new Action(){
			@Override
			public void run(){
				ISelection selection = mainTableViewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				//showMessage("Selection detected on "+ obj.toString() + "Will display description");
				if (obj.toString()!="") showDescription(lblDescription,DESCRIPTION_2);
				if (obj.toString()=="Open SCAU View") showDescription(lblDescription,DESCRIPTION_2);
				if (obj.toString()=="Open Benchmarking View") showDescription(lblDescription,DESCRIPTION_1);
				if (obj.toString()=="Open Metrics View") showDescription(lblDescription,DESCRIPTION_3);
				
				//{ "Open SCAU View", "Open Benchmarking View", "Open Metrics View" };
			
				
				
			}
		};
	}

	private void hookDoubleClickAction() {
		mainTableViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	
	private void hookSelectionAction() {
		mainTableViewer.addSelectionChangedListener(new ISelectionChangedListener(){
			
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				selectionAction.run();
				
			}
		});
	}	
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		mainTableViewer.getControl().setFocus();
	}
	/**
	 * Display a dialogbox with a message.
	 */
	
	private void showMessage(String message) {
		MessageDialog.openInformation(
				mainTableViewer.getControl().getShell(),
			"TSQM Benchmark View",
			message);
	}
	/**
	 * Display a message in Status.
	 */
	public static void showStatusMessage(String message) {
		//TODO: Show our message
		//

		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		writer.println(textStatus.getText());
		writer.println(message);
		String dstring = stringWriter.toString();
		textStatus.setText(dstring);
		
	}
	
	/**
	 * Show the description (on a  label)
	 */
	private String showDescription(Label l, String[] theDescription) {
		String strDesc = "";
		for (String element : theDescription) {
			if (element==""){
				//l.setText(EOL);
				strDesc= strDesc + EOL;
			}else{
				//l.setText(element);
				strDesc= strDesc + element;
			}
		}
		l.setText(strDesc);
		return strDesc;
	}
	
	public static void displaySCAUView() {
		IWorkbenchWindow wbw = PlatformUI.getWorkbench().getWorkbenchWindows()[0];
		IWorkbenchPage pageView = wbw.getActivePage();
		IPerspectiveDescriptor perspectDecribe;
		if (pageView != null) {
			TSQMSCAUViewpart targetView = (TSQMSCAUViewpart) pageView.findView("com.tsqm.plugin.views.TSQMSCAUViewpart");
			if (targetView == null) {
				try {
					pageView.showView("com.tsqm.plugin.views.TSQMSCAUViewpart");
					 perspectDecribe =pageView.getPerspective();
					 showStatusMessage ("The perspective is: " + EOL + perspectDecribe.getLabel());
				} catch (PartInitException e) {
					e.printStackTrace();
					showStatusMessage("Show view error: " + EOL + e.getMessage());
				}
			}
			
			fireArmEvent();
		}
	}

	public static void displayTSQM() {
		IWorkbenchWindow wbw = PlatformUI.getWorkbench().getWorkbenchWindows()[0];
		IWorkbenchPage pageView = wbw.getActivePage();
		IPerspectiveDescriptor perspectDecribe;
		if (pageView != null) {
			TSQMSCAUViewpart targetView = (TSQMSCAUViewpart) pageView.findView("com.tsqm.plugin.views.TSQMSCAUViewpart");
			if (targetView == null) {
				try {
					pageView.showView("com.tsqm.plugin.views.TSQMViewpartSWT");
					 perspectDecribe =pageView.getPerspective();
					 showStatusMessage ("The perspective is: " + EOL + perspectDecribe.getDescription());
				} catch (PartInitException e) {
					e.printStackTrace();
					showStatusMessage("Show view error: " + EOL + e.getMessage());
				}
			}
			
			fireArmEvent();
		}
	}
	
/*
 * Attempt to show the metrics view
 */
	public static void displayMetricsView() {
		IWorkbenchWindow wbw = PlatformUI.getWorkbench().getWorkbenchWindows()[0];
		IWorkbenchPage pageView = wbw.getActivePage();
		IPerspectiveDescriptor perspectDecribe;
		if (pageView != null) {
			TSQMSCAUViewpart targetView = (TSQMSCAUViewpart) pageView.findView("com.tsqm.plugin.views.TSQMSCAUViewpart");
			if (targetView == null) {
				try {
					//pageView.showView("net.sourceforge.metrics.ui.MetricsView");
					//pageView.showView("org.phpsrc.eclipse.pti.tools.phpdepend.ui.views.metricrunner");
					pageView.showView("com.tsqm.plugin.views.TSQMMetricsView");

					//"org.phpsrc.eclipse.pti.tools.phpdepend.ui.PHPDependSummaryView
					 perspectDecribe = pageView.getPerspective();
					 showStatusMessage ("The current perspective is: " + EOL + perspectDecribe.getDescription());
				} catch (PartInitException e) {
					e.printStackTrace();
					showStatusMessage("Show view error: " + EOL + e.getMessage());
				}
			}
			
			fireArmEvent();
		}
	}
	private static void fireArmEvent() {
		if (armListener != null) {
			armListener.widgetArmed(null);
		}
	}

	/**
	 * @return the armListener
	 */
	public static ArmListener getArmListener() {
		return armListener;
	}

	/**
	 * @param armListener the armListener to set
	 */
	public static void setArmListener(ArmListener armListener) {
		TSQMMainView.armListener = armListener;
	}
}
