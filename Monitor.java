import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

public class Monitor extends Thread
{
	boolean isWorking = false;
	String pattern ="";
	Webcam webcam;
	MediaGUI mediaPlayer;
	public static int mpCount =0;
	public static File song1[];
	String song2;
	public static int songcount;
	
	public Monitor(File songs[])
	{
		int i=0;
		song1=songs;
		songcount=song1.length;
		System.out.println("In Monitor length:"+songcount);
		
		while(i<song1.length)
		{
			System.out.println("In Monitor:"+song1[i]);
			i++;
		}
	}
	public Monitor(MediaGUI mediaPlayer, Webcam webcam) 
	{
		// TODO Auto-generated constructor stub
		isWorking = mediaPlayer.mediaPlayer.getMediaPlayer().isPlaying();
		this.webcam = webcam;
		this.mediaPlayer = mediaPlayer;
		start();
		System.out.println("Monitor Thread Strted");
	}

	public void run() 
	{
		while (true) 
		{
			if (isWorking) 
			{
			//	String song;
				File newFile;
			//	System.out.println("checking for pattern");
				pattern = scanPattern(webcam.getImage());
				if (pattern.equals("forward")) 
				{
					mediaPlayer.seekTime(-5000);
				} 
				else if (pattern.equals("reverse"))
				{
					mediaPlayer.seekTime(+5000);
				}
				else if (pattern.equals("pause")) 
				{
					mediaPlayer.mediaPlayer.getMediaPlayer().pause();
					mediaPlayer.pause.setEnabled(false);
					mediaPlayer.ply.setEnabled(true);
				}
				else if (pattern.equals("play")) 
				{
					mediaPlayer.mediaPlayer.getMediaPlayer().play();
					mediaPlayer.ply.setEnabled(false);
					mediaPlayer.pause.setEnabled(true);
				}
				else if(pattern.equals("next"))
				{
					
					System.out.println("Next Song");
					try 
					{	
						System.out.println("Song1 Count:"+songcount);	
						
						if (mpCount>=songcount)
						{	
							System.out.println("Mp3 Count old:"+mpCount);	
							mpCount=songcount;
							System.out.println("Mp3 Count:"+mpCount);	
						}
						else 
						{
							mpCount++;
							newFile = song1[mpCount];
							song2 = newFile.getAbsolutePath();
							System.out.println("Next song:"+song2);
							mediaPlayer.mediaPlayer.getMediaPlayer().playMedia(song2);
							mediaPlayer.tit.setText(song2);
							
						//	MediaGUI mg=new MediaGUI("next constructor");
						//	mg.run1(song2);
						}
					} 
					catch (Exception e) 
					{
						System.out.println("Try again:(Next Song Not Found) " + e.getMessage());
						e.printStackTrace();
					}
					
				}
				else if(pattern.equals("previous"))
				{
					System.out.println("Previous Song");
					try 
					{
						if(mpCount <= 0)
							mpCount = 0;
						else 
						{
							mpCount--;
							newFile = song1[mpCount];
							song2 = newFile.getAbsolutePath();
							mediaPlayer.mediaPlayer.getMediaPlayer().playMedia(song2);
							mediaPlayer.tit.setText(song2);
						//	MediaGUI mg=new MediaGUI("previous constructor");
						//	mg.run1(song2);
							System.out.println("Previous song:"+song2);
						}
					} 
					catch (Exception e) 
					{
						System.out.println("Try again:(Previous Song Not Found) " + e.getMessage());
					}
				}
				else if(pattern.equals("VolUp"))
				{
					int vol = mediaPlayer.mediaPlayer.getMediaPlayer().getVolume();
					if (vol <= 200) {
						int up = vol + 10;
						mediaPlayer.mediaPlayer.getMediaPlayer().setVolume(up);
						System.out.println("volumn up=" + vol);
						System.out.println("volumn update=" + up);
					}
					if(vol==200)
					{
						mediaPlayer.volUp.setEnabled(false);
						
					}
					mediaPlayer.volDown.setEnabled(true);
					mediaPlayer.mute.setEnabled(true);
					
				}
				else if(pattern.equals("VolDown"))
				{
					int vol = mediaPlayer.mediaPlayer.getMediaPlayer().getVolume();
					if (vol >= 0) 
					{
						int down = vol - 10;
						mediaPlayer.mediaPlayer.getMediaPlayer().setVolume(down);
						System.out.println("volumn down=" + vol);
						System.out.println("volumn down update=" + down);
					}
					if(vol==0)
					{
						mediaPlayer.volDown.setEnabled(false);
						mediaPlayer.mute.setEnabled(false);
					}
					mediaPlayer.volUp.setEnabled(true);
					
					
				}
				pattern = "nothing";
			}
			else
			{
				System.out.println("Not Playing");
			}
			
			try 
			{
				Thread.sleep(50);
				isWorking = mediaPlayer.mediaPlayer.getMediaPlayer().isPlayable();
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
	private String scanPattern(BufferedImage image) 
	{
		int height = image.getHeight(),width = image.getWidth();
		pattern = "nothing";
		for(int i=0;i<width;i++)
		{
			for(int j=0;j<height;j++)
			{
				Color pixel = new Color(image.getRGB(i, j));
				if(pixel.getRed()>150 && pixel.getBlue()<100&&pixel.getGreen()<100)
				{
			//		System.out.println("Red Found at "+i+" "+j);
					if(i>(width*0.75))
					{
						pattern = "forward";
						System.out.println(pattern);
					}
					else if(i<(width*0.25))
					{
						pattern = "reverse";
						System.out.println(pattern);
					}
				}
				int flg=0;
				if(pixel.getRed()<100 && pixel.getBlue()>150&&pixel.getGreen()<100)
				{
				//	System.out.println("Blue Found at "+i+" "+j);
					if(i<(width*0.60)&&i>(width*0.40))
					{
						if(flg==0)
						{	
							pattern = "pause";
							System.out.println(pattern);
							
							try {
								sleep(1000);
							} catch (InterruptedException e) 
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							flg=1;
							break;
						}
						if(flg==1)
						{
							pattern = "play";
							System.out.println(pattern);
							try 
							{
								sleep(1000);
							} catch (InterruptedException e) 
							{
								e.printStackTrace();
							}
							break;
						}
					
					}
				}
				if(pixel.getRed()<100 && pixel.getBlue()<100&&pixel.getGreen()>150)
				{
				//	System.out.println("Green Found at "+i+" "+j);
					if(i>(width*0.75))
					{
						pattern ="previous";
						System.out.println(pattern);
						try {
							sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(i<(width*0.25))
					{
						pattern = "next";
						System.out.println(pattern);
						try {
							sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(i<(width*0.60)&&i>(width*0.40)&&j<height*0.25)
					{
						pattern="VolUp";
						System.out.println("VolUp");
						try {
							sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					if(i<(width*0.60)&&i>(width*0.40)&&j>height*0.80)
					{
						pattern="VolDown";
						System.out.println("VolDown");
						try {
							sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			
			}
		}
		
		return pattern;
		// TODO Auto-generated method stub

	}
	
}
