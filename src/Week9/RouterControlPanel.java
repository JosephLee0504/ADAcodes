package Week9;

/**
   A class that prepares a gui control panel for a unicast router
   @see UnicastRouter.java
*/
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import javax.swing.event.ListSelectionEvent;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class RouterControlPanel extends JPanel
   implements ActionListener, ListSelectionListener, RouterDisplay
{
   private UnicastRouter router;
   private JTextArea displayArea, inputArea;
   @SuppressWarnings("rawtypes")
   private DefaultListModel adjacentRoutersListModel, networkListModel;
   @SuppressWarnings("rawtypes")
   private JList adjacentRoutersList, networkList; // hold InetAndAddress objects
   private JButton exitButton, addAdjacentButton, relaxAdjacentButton, sendButton;
   
   @SuppressWarnings({ "rawtypes", "unchecked" })
   public RouterControlPanel(UnicastRouter router)
   {  super(new GridLayout(2,2));
      this.router = router;
      setPreferredSize(new Dimension(800, 500));
      
      // prepare the router panel
      JPanel routerPanel = new JPanel();
      routerPanel.setLayout(new BoxLayout(routerPanel, BoxLayout.Y_AXIS));
      routerPanel.setBorder(BorderFactory.createTitledBorder("Router"));
      routerPanel.add(new JLabel("IP address: " + router.getRouterAddress()));
      routerPanel.add(new JLabel("Port: " + router.getRouterPort()));
      routerPanel.add(Box.createVerticalGlue());
      exitButton = new JButton("Exit");
      exitButton.addActionListener(this);
      routerPanel.add(exitButton);
      
      // prepare the display panel
      JPanel displayPanel = new JPanel(new BorderLayout());
      displayPanel.setBorder(BorderFactory.createTitledBorder("Display"));
      displayArea = new JTextArea(5, 10);
      displayArea.setEditable(false);
      JPanel displaySubPanel = new JPanel(new BorderLayout());
      displaySubPanel.add(new JLabel("History:"), BorderLayout.NORTH);
      displaySubPanel.add(new JScrollPane(displayArea), BorderLayout.CENTER);
      displayPanel.add(displaySubPanel, BorderLayout.CENTER);
      inputArea = new JTextArea(5, 10);
      inputArea.setEditable(true);
      JPanel inputSubPanel = new JPanel(new BorderLayout());
      inputSubPanel.add(new JLabel("Message to send:"), BorderLayout.NORTH);
      inputSubPanel.add(new JScrollPane(inputArea), BorderLayout.CENTER);
      displayPanel.add(inputSubPanel, BorderLayout.SOUTH);
      
      // prepare the adjacent router panel
      JPanel adjacentPanel = new JPanel(new BorderLayout());
      adjacentPanel.setBorder(BorderFactory.createTitledBorder("Adjacent Routers"));
      adjacentRoutersListModel = new DefaultListModel();
      adjacentRoutersList = new JList(adjacentRoutersListModel);      
      adjacentRoutersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      adjacentRoutersList.addListSelectionListener(this);
      adjacentPanel.add(new JScrollPane(adjacentRoutersList), BorderLayout.CENTER);
      JPanel adjacentSubPanel = new JPanel(); // default FlowLayout
      addAdjacentButton = new JButton("Add Router");
      addAdjacentButton.addActionListener(this);
      addAdjacentButton.setEnabled(true);
      relaxAdjacentButton = new JButton("Relax Router");
      relaxAdjacentButton.addActionListener(this);
      relaxAdjacentButton.setEnabled(false);
      adjacentSubPanel.add(addAdjacentButton);
      adjacentSubPanel.add(relaxAdjacentButton);
      adjacentPanel.add(adjacentSubPanel, BorderLayout.SOUTH);
      
      // prepare the network panel
      JPanel networkPanel = new JPanel(new BorderLayout());
      networkPanel.setBorder(BorderFactory.createTitledBorder("Network"));
      networkListModel = new DefaultListModel();
      networkList = new JList(networkListModel);
      networkList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      networkList.addListSelectionListener(this);
      networkPanel.add(new JScrollPane(networkList), BorderLayout.CENTER);
      JPanel networkSubPanel = new JPanel(); // default FlowLayout
      sendButton = new JButton("Send Message");
      sendButton.addActionListener(this);
      sendButton.setEnabled(false);
      networkSubPanel.add(sendButton);
      networkPanel.add(networkSubPanel, BorderLayout.SOUTH);
      
      // add the four principal panels to this panel
      add(routerPanel);
      add(displayPanel);
      add(adjacentPanel);
      add(networkPanel);
   }
   
   public void actionPerformed(ActionEvent event)
   {  Object source = event.getSource();
      if (source == exitButton)
      {  router.requestStop();
         // dispose of all frames created by this application
         Frame[] frames = Frame.getFrames();
         if (frames != null)
         {  for (int i=0; i<frames.length; i++)
               frames[i].dispose();
         }
      }
      else if (source == addAdjacentButton)
      {  ConnectionPanel connectionPanel = new ConnectionPanel(router);
         connectionPanel.displayPanel();
      }
      else if (source == relaxAdjacentButton)
      {  IPAndDistance selectedValue
            = (IPAndDistance)adjacentRoutersList.getSelectedValue();
         if (selectedValue != null)
         {  InetAddress destination = selectedValue.inetAddress;
            UnicastUpdatePacket sendPacket = new UnicastUpdatePacket
               (router.getRouterAddress(), destination,
               router.getDistanceVector());
            router.processPacket(sendPacket);
         }
      }
      else if (source == sendButton)
      {  
         IPAndDistance selectedValue
            = (IPAndDistance)networkList.getSelectedValue();
         if (selectedValue != null)
         {  InetAddress destination = selectedValue.inetAddress;
            UnicastMessagePacket sendPacket = new UnicastMessagePacket
               (router.getRouterAddress(), destination,
               inputArea.getText());
            router.processPacket(sendPacket);
         }
      }
   }
   
   public void valueChanged(ListSelectionEvent event)
   {  Object source = event.getSource();
      if (source == adjacentRoutersList)
      {  // check whether relaxAdjacentButton should be enabled or disabled
         if (adjacentRoutersList.isSelectionEmpty())
             relaxAdjacentButton.setEnabled(false);
         else
             relaxAdjacentButton.setEnabled(true);
      }
      else if (source == networkList)
      {  // check whether sendButton should be enabled or disabled
         if (networkList.isSelectionEmpty())
            sendButton.setEnabled(false);
         else
            sendButton.setEnabled(true);
      }
   }

   public void displayText(final String text)
   {  EventQueue.invokeLater(new Runnable()
         {  public void run()
            {  displayArea.append(text);
            }
         });
   }
   
   public void displayAdjacentRouters
      (final Map<InetAddress, RouterConnection> adjacentRouters)
   {  EventQueue.invokeLater(new Runnable()
         {  public void run()
            {  prepareAdjacentRoutersList(adjacentRouters);
            }
         });
   }
   
   public void displayNetwork(final RouterDistanceVector distanceVector)
   {  EventQueue.invokeLater(new Runnable()
         {  public void run()
            {  prepareNetworkList(distanceVector);
            }
         });
   }
   
   // helper method that actually modifies adjacentRoutersList
   // from within the event dispatch thread
   @SuppressWarnings("unchecked")
   private void prepareAdjacentRoutersList
      (Map<InetAddress, RouterConnection> adjacentRouters)
   {  adjacentRoutersListModel.clear();
      for (InetAddress router : adjacentRouters.keySet())
      {  RouterConnection connection = adjacentRouters.get(router);
         adjacentRoutersListModel.addElement(new IPAndDistance(router,connection.getDistance()));
      }
   }
   
   // helper method that actually modifies networkList
   // from within the event dispatch thread
   @SuppressWarnings("unchecked")
   private void prepareNetworkList(RouterDistanceVector distanceVector)
   {  networkListModel.clear();
      for (InetAddress router : distanceVector.getAllRouters())
      {  double distance = distanceVector.getDistanceToDestination(
            router);
         networkListModel.addElement(new IPAndDistance(router, distance));
      }
   }
   
   // inner class that represents an InetAddress and a double distance
   // for display in the lists
   private class IPAndDistance
   {
      public InetAddress inetAddress;
      public double distance;
      
      public IPAndDistance(InetAddress inetAddress, double distance)
      {  this.inetAddress = inetAddress;
         this.distance = distance;
      }
      
      public String toString()
      {  if (inetAddress != null)
            return inetAddress.toString() + " (" + distance + ")";
         else
            return "no address";
      }
   }
   
   // inner class that prepares a gui panel for establishing
   // a connection to an adjacent router
   private class ConnectionPanel extends JPanel
      implements ActionListener, Runnable
   {
      private static final int DEFAULT_PORT = 7777;
      private static final double DEFAULT_DISTANCE = 1.0;
      private UnicastRouter router;
      private JTextField addressField, portField, distanceField;
      private JFrame frame;
      private JButton connectButton, cancelButton;
      
      public ConnectionPanel(UnicastRouter router)
      {  super();
         this.router = router;
         setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
         setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
         
         // prepare the address panel
         JPanel addressPanel = new JPanel(new BorderLayout());
         addressPanel.add(new JLabel("IP Address: "), BorderLayout.WEST);
         addressField = new JTextField("", 20);
         addressPanel.add(addressField, BorderLayout.EAST);
         
         // prepare the port panel
         JPanel portPanel = new JPanel(new BorderLayout());
         portPanel.add(new JLabel("IP port: "), BorderLayout.WEST);
         portField = new JTextField(""+DEFAULT_PORT, 5);
         portPanel.add(portField, BorderLayout.EAST);
         
         // prepare the distance panel
         JPanel distancePanel = new JPanel(new BorderLayout());
         distancePanel.add(new JLabel("Distance: "), BorderLayout.WEST);
         distanceField = new JTextField(""+DEFAULT_DISTANCE, 5);
         distancePanel.add(distanceField, BorderLayout.EAST);
         
         // prepare the address panel
         JPanel buttonPanel = new JPanel(); // default FlowLayout
         connectButton = new JButton("Connect");
         connectButton.addActionListener(this);
         cancelButton = new JButton("Cancel");
         cancelButton.addActionListener(this);
         buttonPanel.add(connectButton);
         buttonPanel.add(cancelButton);
         
         // add the four panels to this panel
         add(addressPanel);
         add(portPanel);
         add(distancePanel);
         add(buttonPanel);
      }
      
      // displays the panel in a frame, intended to be run in the 
      // event dispatch thread (since its displaying a gui)
      public void displayPanel()
      {  frame = new JFrame();
         frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         frame.getContentPane().add(this);
         frame.pack();
         frame.setResizable(false);
         // position the frame in the middle of the screen
         Toolkit tk = Toolkit.getDefaultToolkit();
         Dimension screenDimension = tk.getScreenSize();
         Dimension frameDimension = frame.getSize();
         frame.setLocation((screenDimension.width-frameDimension.width)/2,
            (screenDimension.height-frameDimension.height)/2);
         frame.setVisible(true);
      }
      
      
      public void actionPerformed(ActionEvent event)
      {  Object source = event.getSource();
         if (source == connectButton)
         {  // perform the run method in a separate thread
            frame.dispose();  
            Thread thread = new Thread(this);
            thread.start();
         }
         else if (source == cancelButton && frame != null)
            frame.dispose();  
      }
      
      // run method for making the connection intended to be run in a
      // separate thread and not in the event dispatch thread
      public void run()
      {  String routerAddress = getAddress();
         int routerPort = getPort();
         double routerDistance = getDistance();
         try
         {  router.connectToIP(routerAddress, routerPort, routerDistance);
         }
         catch (IOException e)
         {  JOptionPane.showMessageDialog(this,
               "Unable to establish connection",
               "IO exception", JOptionPane.ERROR_MESSAGE);
         }
      }

      public String getAddress()
      {  return addressField.getText();
      }
      
      public int getPort()
      {  int port = DEFAULT_PORT;
         try
         {  port = Integer.parseInt(portField.getText());
         }
         catch (NumberFormatException e)
         {  JOptionPane.showMessageDialog(this, "Invalid Port, using default",
               "Invalid Port", JOptionPane.INFORMATION_MESSAGE);
         }
         return port;
      }
      
      public double getDistance()
      {  double distance = DEFAULT_DISTANCE;
         try
         {  distance = Double.parseDouble(distanceField.getText());
         }
         catch (NumberFormatException e)
         {  JOptionPane.showMessageDialog(this, "Invalid Distance, using default",
               "Invalid Distance", JOptionPane.INFORMATION_MESSAGE);
         }
         return distance;
      }
   }
}
