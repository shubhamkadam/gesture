import javax.swing.JSlider;


public class Timer extends Thread
{
	MediaGUI mediagui;
	JSlider js;
	static double factor;
	public Timer(MediaGUI mediaGUI) 
	{
		this.mediagui = mediaGUI;
		js = mediagui.js;
	}
	public void run() 
	{
		System.out.println("Timer started");
		while(true)
		{
			if(mediagui.mediaPlayer.getMediaPlayer().isPlayable())
			{
				factor = mediagui.mediaPlayer.getMediaPlayer().getLength()/100;//100 full
						//59   x; x = 59*(full/100);
				js.setEnabled(true);
				js.setMaximum(100);
				js.setMinimum(0);
				js.setValue( (int) (mediagui.mediaPlayer.getMediaPlayer().getTime() /factor));
				mediagui.cnt = 0;
			}
			else
			{
				js.setMaximum(0);
				js.setMinimum(0);
				js.setValue(0);
				js.setEnabled(false);
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
