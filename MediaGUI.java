import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Canvas;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamImageTransformer;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.util.jh.JHGrayFilter;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.LibVlcFactory;
import uk.co.caprica.vlcj.logger.Logger;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.player.DefaultMediaPlayer;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;

public class MediaGUI extends JFrame implements ActionListener, ChangeListener ,MouseListener,MouseMotionListener
{
	File newFile;
	String song1;
	String song;
	GestureWebCam camera;
	JMenuBar mb;
	JMenu file, play, help;
	JMenuItem open, gesture, history, exit;
	JSlider js;
	JButton pre, ply, pause, next, forward, backward;
	JButton open1;
	JButton volUp, volDown, webcam,mute;
	MediaPlayerFactory mediaPlayerFactory;
	EmbeddedMediaPlayerComponent mediaPlayer;
	String mediaPath;
	JLabel tit;
	JPanel viewMedia = new JPanel();
	int key1, ctrl1;
	File dir;
	File mp3files[];
	int mp3Count = 0, flg = 0;
	long count=0;
	static String vlcPath ="C:\\Program Files (x86)\\VideoLAN\\VLC";
	JPanel panel2;
	int currentTime, totleTime;
	Thread timer;
	MediaGUI(String ref)
	{
		System.out.println("In Constructor MediaGUI:"+ref);
	}
	MediaGUI() 
	{
		super("SAN(Gesture Media Player)");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1370, 730);
		setResizable(true);
		setLocation(0, 0);
		//setFocusable(true);

		panel2 = new JPanel();
		panel2.setLayout(null);
		panel2.setLayout(new FlowLayout(FlowLayout.LEFT));

		ImageIcon pr = new ImageIcon("21.png");
		ImageIcon pl = new ImageIcon("24.png");
		ImageIcon pu = new ImageIcon("23.png");
		ImageIcon nx = new ImageIcon("22.png");
		ImageIcon op = new ImageIcon("open1.png");
		ImageIcon forwa = new ImageIcon("29.png");
		ImageIcon backw = new ImageIcon("28.png");
		ImageIcon voup = new ImageIcon("up.png");
		ImageIcon vodow = new ImageIcon("down.png");
		ImageIcon muteic = new ImageIcon("mute.png");
		ImageIcon ges = new ImageIcon("gesture.png");
		
		open1 = new JButton(op);
		open1.setActionCommand("Open");
		open1.setSize(40, 40);
		open1.addActionListener(this);

		js = new JSlider();

		// js.setBounds(0,0,600,20);
		js.setMajorTickSpacing(1);
		js.setMinorTickSpacing(1);
		js.setPaintTicks(true);
	//	js.setPaintLabels(true);
		js.setMinimum(0);
		js.setValue(0);
		js.setValueIsAdjusting(true);
		js.addChangeListener(this);

		pre = new JButton(pr);
		pre.setActionCommand("pre");
		ply = new JButton(pl);
		ply.setActionCommand("play");
		pause = new JButton(pu);
		pause.setActionCommand("pause");
		next = new JButton(nx);
		next.setActionCommand("next");
		forward = new JButton(forwa);
		forward.setActionCommand("Forward");
		backward = new JButton(backw);
		backward.setActionCommand("Backward");
		webcam = new JButton(ges);
		webcam.setActionCommand("GestureOpen");
		tit=new JLabel("SAN  (Gesture Media Player)");
		tit.setForeground(Color.LIGHT_GRAY);
		volUp = new JButton(voup);
		volUp.setActionCommand("up");

		volDown = new JButton(vodow);
		volDown.setActionCommand("down");
		mute = new JButton(muteic);
		mute.setActionCommand("Mute");

		pre.addActionListener(this);
		ply.addActionListener(this);
		pause.addActionListener(this);
		next.addActionListener(this);
		forward.addActionListener(this);
		backward.addActionListener(this);
		volUp.addActionListener(this);
		volDown.addActionListener(this);
		webcam.addActionListener(this);
		mute.addActionListener(this);
		
		panel2.addMouseListener(this);
		panel2.addMouseMotionListener(this);
		
		
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcPath);
		mediaPlayer = new EmbeddedMediaPlayerComponent();
		this.setContentPane(mediaPlayer);
		mediaPlayer.setCursorEnabled(true);
		mediaPlayer.getMediaPlayer().setFullScreen(true);
	
		panel2.add(open1);
		panel2.add(pre);
		panel2.add(backward);
		panel2.add(ply);
		panel2.add(pause);
		panel2.add(forward);
		panel2.add(next);
		panel2.add(js);
		panel2.add(mute);
		panel2.add(volDown);
		panel2.add(volUp);
		panel2.add(webcam);
		panel2.add(tit);
		//panel2.setEnabled(true);
		panel2.setBackground(Color.BLACK);
		this.add(panel2, BorderLayout.SOUTH);
	//	panel2.setVisible(false);
	/*	file = new JMenu("File");
		file.setMnemonic('F');
		file.addSeparator();
		key1 = KeyEvent.VK_O;
		ctrl1 = InputEvent.CTRL_MASK;
		file.add(makeMenuItem("Open", key1, ctrl1));
		key1 = KeyEvent.VK_G;
		ctrl1 = InputEvent.CTRL_MASK;
		file.add(makeMenuItem("Open Gesture", key1, ctrl1));
		key1 = KeyEvent.VK_R;
		ctrl1 = InputEvent.CTRL_MASK;
		file.add(makeMenuItem("Recent Played", key1, ctrl1));
		key1 = KeyEvent.VK_X;
		ctrl1 = InputEvent.CTRL_MASK;
		file.add(makeMenuItem("Exit...", key1, ctrl1));

		play = new JMenu("Play");
		play.setMnemonic('P');
		play.addSeparator();
		help = new JMenu("Help");
		help.setMnemonic('H');
		help.addSeparator();
		help.add(makeMenuItem("About", key1, ctrl1));
	*/
		timer = new Timer(this);
		timer.start();
	}

	private JMenuItem makeMenuItem(String name, int key, int ctrl) {
		JMenuItem m = new JMenuItem(name);
		KeyStroke ctrlH = KeyStroke.getKeyStroke(key, ctrl);
		m.setAccelerator(ctrlH);
		m.addActionListener(this);
		return m;
	}

	public void seekTime(int time)
	{
		long tm = mediaPlayer.getMediaPlayer().getTime();
		System.out.println("In Forward time=" + tm);
		mediaPlayer.getMediaPlayer().setTime(tm + time);
	}

	public void actionPerformed(ActionEvent event) 
	{
		String ref = event.getActionCommand();
		if (ref.equals("Open")) 
		{
			String mediaPath = "";
			File fileUrl;
			JFileChooser fileSelect = new JFileChooser();
			fileSelect.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int status = fileSelect.showOpenDialog(MediaGUI.this);

			fileUrl = fileSelect.getSelectedFile();
			mediaPath = fileUrl.getAbsolutePath();
			run1(mediaPath);

			if (status == JFileChooser.APPROVE_OPTION) 
			{
				File file = fileSelect.getSelectedFile();
				mediaPath = file.getAbsolutePath();
				dir = file.getParentFile();
				mp3files = dir.listFiles();
				mp3Count = 0;
				for (int i = 0; i < mp3files.length; i++) 
				{
					System.out.println("Songs=" + mp3files[i]);	
				}
				Monitor mn=new Monitor(mp3files);
			}

		}

		String song;
		File newFile;
		if (ref.equals("pre")) {
			try {
				if (mp3Count <= 0)
					mp3Count = 0;
				else {
					mp3Count--;
					newFile = mp3files[mp3Count];
					song = newFile.getAbsolutePath();
					run1(song);
				}
			} 
			catch (Exception e)
			{
				System.err.println("Try again:(Previous Song Not Found) " + e);
			}

		}
		String song1;
		if (ref.equals("next")) 
		{

			try {
				if (mp3Count >= mp3files.length)
					mp3Count = mp3files.length;
				else {
					mp3Count++;
					newFile = mp3files[mp3Count];
					song1 = newFile.getAbsolutePath();
					run1(song1);
				}
			} catch (Exception e) {
				System.err.println("Try again:(Next Song Not Found) " + e);
			}

		}

		if (ref.equals("pause"))
		{
			mediaPlayer.getMediaPlayer().pause();
			// System.out.println("len:"+mediaPlayer.getMediaPlayer().getTitle());
			System.out.println("len:"
					+ mediaPlayer.getMediaPlayer().getLength());
			// System.out.println("len:"+mediaPlayer.getMediaPlayer().getMediaMeta());
			pause.setEnabled(false);
			ply.setEnabled(true);
		}
		if (ref.equals("play")) 
		{
			mediaPlayer.getMediaPlayer().play();
			ply.setEnabled(false);
			pause.setEnabled(true);
		}
		if (ref.equals("Forward"))
		{
			/*
			 * long tm = mediaPlayer.getMediaPlayer().getTime();
			 * System.out.println("In Forward time=" + tm);
			 * mediaPlayer.getMediaPlayer().setTime(tm + 5000);
			 */
			seekTime(5000);
		}
		if (ref.equals("Backward")) {
			/*
			 * long tm = mediaPlayer.getMediaPlayer().getTime();
			 * System.out.println("In Backward time=" + tm);
			 * mediaPlayer.getMediaPlayer().setTime(tm - 5000);
			 */
			seekTime(-5000);
		}
		if (ref.equals("Mute")) 
		{
				mediaPlayer.getMediaPlayer().setVolume(0);
				mute.setEnabled(false);
		}

		if (ref.equals("up")) 
		{
			mute.setEnabled(true);
			int vol = mediaPlayer.getMediaPlayer().getVolume();
			if (vol<=200)
			{
				int up = vol + 10;
				mediaPlayer.getMediaPlayer().setVolume(up);
				System.out.println("volumn up=" + vol);
				System.out.println("volumn update=" + up);
			}
			if(vol==200)
			{
				volUp.setEnabled(false);
			}
			volDown.setEnabled(true);
			mute.setEnabled(true);
			

		}
		if (ref.equals("down")) 
		{
			int vol = mediaPlayer.getMediaPlayer().getVolume();
			if (vol >= 0) 
			{
				int down = vol - 10;
				mediaPlayer.getMediaPlayer().setVolume(down);
				System.out.println("volumn down=" + vol);
				System.out.println("volumn down update=" + down);
			}
			if(vol==0)
			{
				volDown.setEnabled(false);
				mute.setEnabled(false);
			}
			volUp.setEnabled(true);
			
		}
		if (ref.equals("GestureOpen")) 
		{
			if (camera == null) 
			{
				camera = new GestureWebCam(this);
				System.out.println("Camera Started");
			} else 
			{
				System.out.println("Already open");
			}
			// new NewCam();
			// new NewJFrame().setVisible(true);

		}
	}

	public void Wifi(String recstr) 
	{
		if (recstr.equals("PlaySong"))
		{
			mediaPlayer.getMediaPlayer().play();
		}
		if (recstr.equals("NextSong")) 
		{
			try 
			{
				if (mp3Count >= mp3files.length)
					mp3Count = mp3files.length;
				else 
				{
					mp3Count++;
					newFile = mp3files[mp3Count];
					song1 = newFile.getAbsolutePath();
					run1(song1);
				}
			} 
			catch (Exception e) 
			{
				System.err.println("Try again:(Next Song Not Found) " + e);
			}

		}
		if(recstr.equals("PreviousSong"))
		{
			try 
			{
				if (mp3Count <= 0)
					mp3Count = 0;
				else 
				{
					mp3Count--;
					newFile = mp3files[mp3Count];
					song = newFile.getAbsolutePath();
					run1(song);
				}
			} 
			catch (Exception e) 
			{
				System.err.println("Try again:(Previous Song Not Found) " + e);
			}
		}
		if (recstr.equals("PauseSong"))
		{
			mediaPlayer.getMediaPlayer().pause();
		}
		if (recstr.equals("Forward")) 
		{
		/*	long tm = mediaPlayer.getMediaPlayer().getTime();
			System.out.println("In Forward time=" + tm);
			mediaPlayer.getMediaPlayer().setTime(tm + 5000);
		*/	seekTime(5000);
			
		}
		if (recstr.equals("Backward")) 
		{
		/*	long tm = mediaPlayer.getMediaPlayer().getTime();
			System.out.println("In Backward time=" + tm);
			mediaPlayer.getMediaPlayer().setTime(tm - 5000);
		*/	seekTime(-5000);
			
		}
		if (recstr.equals("VolUp")) 
		{
			int vol = mediaPlayer.getMediaPlayer().getVolume();
			if (vol <= 200) {
				int up = vol + 10;
				mediaPlayer.getMediaPlayer().setVolume(up);
				System.out.println("volumn up=" + vol);
				System.out.println("volumn update=" + up);
			}
		}
		if (recstr.equals("VolDown")) 
		{
			int vol = mediaPlayer.getMediaPlayer().getVolume();
			if (vol >= 0) 
			{
				int down = vol - 10;
				mediaPlayer.getMediaPlayer().setVolume(down);
				System.out.println("volumn down=" + vol);
				System.out.println("volumn down update=" + down);
			}
		}
	}

	public void run1(String medp) 
	{
		System.out.println("Song Receive:"+medp);
		tit.setText(medp);
		try
		{
			String ref1=medp;
			String re="\\";
			String[] titles=ref1.split(re);
			int i=titles.length;
			System.out.println("split:"+titles[0]);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			this.mediaPlayer.getMediaPlayer().playMedia(medp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try 
		{
			Thread.sleep(100);
		} 
		catch (Exception e) 
		{
		}
		try
		{
		long tm = this.mediaPlayer.getMediaPlayer().getLength();
		String ref = "" + tm;
		System.out.println("leng ref:" + ref);
		int le = ref.length();
		String f = ref.substring(0, le - 3);
		int inew = Integer.parseInt(f);
		js.setMaximum(inew);
		System.out.println("leng final:" + inew + "  min=" + js.getMinimum()+ "  max=" + js.getMaximum());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args) 
	{

		final MediaGUI medGui = new MediaGUI();
		medGui.setVisible(true);
		Thread t = new Thread() 
		{
			public void run()
			{
				System.out.println("Server ready to listen.....");
				try
				{
					ServerSocket ss = new ServerSocket(1006);
					while (true) 
					{
						Socket s = ss.accept();
						DataInputStream di = new DataInputStream(s.getInputStream());
						String rev = di.readUTF();
						medGui.Wifi(rev);
						System.out.println("Received from Client:" + rev);
						di.close();
						s.close();
					}
				} catch (Exception e) 
				{
				//	e.printStackTrace();
				}
			}
		};
		t.start();

	}
	int cnt=0;
	@Override
	public void stateChanged(ChangeEvent e) 
	{
		cnt++;
	//	System.out.println("state changed");
		JSlider slider = (JSlider) e.getSource();
		if(cnt%2==0) 
		mediaPlayer.getMediaPlayer().setTime((long) (slider.getValue()*Timer.factor));
	}
	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		// TODO Auto-generated method stub
		int x=arg0.getX();
		int y=arg0.getY();
//		System.out.println("In Click X="+x+"  Y="+y);
	
	}
	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		int x=arg0.getX();
		int y=arg0.getY();
	//	System.out.println("X="+x+"  Y="+y);
	//	System.out.println("Mouse Entered");
	//	panel2.setVisible(true);
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		//panel2.setVisible(false);
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent me) 
	{
		int mouseX=me.getX();
		int mouseY=me.getY();
	//	System.out.println("Arrow Location X:"+mouseX+" Y:"+mouseY);
		
	}
}


class GestureWebCam implements WebcamImageTransformer, WebcamMotionListener 
{
	private static final JHGrayFilter RGB = new JHGrayFilter();
	Webcam webcam;

	public BufferedImage transform(BufferedImage image)
	{
		return image;
	}

	Monitor monitor;

	public GestureWebCam(MediaGUI mediaPlayer) 
	{

		webcam = Webcam.getWebcams().get(1);
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		webcam.setImageTransformer((WebcamImageTransformer) this);
		webcam.open();

		monitor = new Monitor(mediaPlayer, webcam);

		JFrame window = new JFrame("Gesture Control");
		WebcamPanel panel = new WebcamPanel(webcam);

//		panel.setFPSDisplayed(true);
		panel.setFillArea(true);
		window.add(panel);
		window.pack();
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		WebcamMotionDetector detector = new WebcamMotionDetector(Webcam.getDefault());
		detector.setInterval(1000); // check every 1000 microsec
		detector.addMotionListener((WebcamMotionListener) this);
		detector.start();

	}
	
	int i = 0;
	public void motionDetected(WebcamMotionEvent wme) 
	{
		i++;
		System.out.println("Motion is Detected" + i);
	}
}
