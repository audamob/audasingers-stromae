package com.audamob.audasingers.stromae.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.audamob.audasingers.stromae.fragment.MainContainerActivityTest;
import com.audamob.audasingers.stromae.model.News;
import com.audamob.audasingers.stromae.tool.db.FtpAccountRecover;
import com.audamob.audasingers.stromae.view.services.impl.ServiceMusicUpdater;
import com.audamob.audasingers.stromae.view.services.impl.ServiceNewsUpdater;
import com.audamob.audasingers.stromae.view.services.impl.ServiceVersionChecker;
import com.audamob.audasingers.stromae.view.services.impl.ServiceVideoUpdater;
import com.widdit.lockScreenShell.HomeBaseBootstrap;

//import com.pad.android.xappad.*;

public class Home extends Activity {
	/** Called when the activity is first created. */
	ArrayList<News> List_News = new ArrayList<News>();

	Activity HomeActivity;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	private boolean isMyServiceRunning(String serviceClassName) {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<RunningServiceInfo> services = manager
				.getRunningServices(Integer.MAX_VALUE);
		for (int i = 0; i < services.size(); i++) {

			if (serviceClassName.equals(services.get(i).service.getClassName())) {
				{

					return true;
				}

			}
		}

		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.auda_layout_temporaire);
		new HomeBaseBootstrap(getApplicationContext());
		HomeActivity = this;
		List_News = new ArrayList<News>();
		try {
			List_News = restore_News();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (List_News.size() == 0) {
			CreateNewsList();
			try {
				sauvegard_News(List_News);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (isMyServiceRunning("com.audamob.audasingers.view.services.impl.ServiceNewsUpdater")) {
			Log.i("UpdateListNews", "is running ");
		} else {
			// Starting ServiceNewsUpdater ...
			Log.i("NEWS", "Starting ServiceNewsUpdater ...");
			Intent i = new Intent(getApplicationContext(),
					ServiceNewsUpdater.class);
			startService(i);
		}

		if (isMyServiceRunning("com.audamob.audasingers.view.services.impl.ServiceMusicUpdater")) {
			Log.i("ServiceMusicUpdater", "is running ");
		} else {
			// Starting ServiceMusicUpdater ...
			Log.i("MUSICS", "Starting ServiceMusicUpdater ...");
			Intent iMusic = new Intent(this, ServiceMusicUpdater.class);
			startService(iMusic);
		}

		if (isMyServiceRunning("com.audamob.audasingers.view.services.impl.ServiceVideoUpdater")) {
			Log.i("ServiceVideoUpdater", "is running ");
		} else {
			// Starting ServiceVideoUpdater ...
			Log.i("VIDOES", "Starting ServiceVideoUpdater ...");
			Intent iVideo = new Intent(this, ServiceVideoUpdater.class);
			startService(iVideo);
		} 
		
		//Call the FTPAccountRecover ...
		Log.i("ACCOUNT","Starting FTPAccountRecover ...");
		new FtpAccountRecover(this);
		
		//Call the ServiceVersionChecker ...
		Log.i("VERSION","Starting Version Checker Service ...");
		ServiceVersionChecker svc = new ServiceVersionChecker(this);
		Log.i("VERSION","is Up to date : "+svc.isUpToDate);


		Intent i = new Intent(Home.this, MainContainerActivityTest.class);
		i.putExtra("updateversion", svc.isUpToDate);
		startActivity(i);
		HomeActivity.finish();

	}

	final Handler handlerUpdater = new Handler() {
		public void handleMessage(Message msg) {
			Log.i("News", "size of List : " + List_News.size());
			try {
				sauvegard_News(List_News);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				File myFile = new File("/sdcard/listnews.txt");
				myFile.createNewFile();

				FileOutputStream fOut = new FileOutputStream(myFile);
				OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
				for (int i = 0; i < List_News.size(); i++) {
					Log.i("ExternalFile", " write ");
					myOutWriter.append("News temp=new News(|||"
							+ List_News.get(i).getTitle() + "|||,|||"
							+ List_News.get(i).getDate() + "|||,|||"
							+ List_News.get(i).getDesccreption() + "|||,|||"
							+ List_News.get(i).getUrl() + "|||,|||"
							+ List_News.get(i).getWriter() + "|||,|||"
							+ List_News.get(i).getImageUrl() + "|||) ;"
							+ "List_News.add(temp);\n\n");
				}

				myOutWriter.close();
				fOut.close();

			} catch (IOException e) {
				Log.i("ExternalFile", " write exception");
			}

			Intent i = new Intent(Home.this, MainContainerActivityTest.class);
			startActivity(i);

			HomeActivity.finish();
		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	ArrayList<News> NewsList;

	private class ThreadUpdateNews extends Thread {
		Handler mHandler;
		boolean Done = false;

		ThreadUpdateNews(Handler h) {
			mHandler = h;
		}

		public void run() {
			Elements content = null;
			try {

				String UrlLyrics = "http://www.mtv.com/artists/lil-wayne/mtv-news/";
				org.jsoup.nodes.Document doc = Jsoup.connect(UrlLyrics).get();
				content = doc.getElementsByClass("metadata");

				Log.i("News", "nombre : " + content.size());
				String title, date, description, imageUrl, url, writer;

				for (int compteur = 0; (compteur < content.size() && List_News
						.size() < 20); compteur++) {
					Element link = content.get(compteur);
					Elements element = link.getElementsByClass("title");
					title = element.text();
					Elements e = ((Jsoup.parse(element.toString()))
							.getElementsByTag("a"));

					url = e.attr("href");

					description = "";
					imageUrl = "";
					element = link.getElementsByClass("dateFormatted");

					writer = element.text();

					date = element.outerHtml().substring(
							element.outerHtml().indexOf("<br />") + 6,
							element.outerHtml().indexOf("</div>"));

					writer.replace(date, "");
					if (writer.contains("MTV News")) {
						writer = "MTV News";
						fetch_MTV_News(title, date, description, url, writer,
								imageUrl);
					}
					if (writer.contains("MTV Buzzworthy")) {
						writer = "MTV Buzzworthy";
						fetch_MTV_Buzzworthy(title, date, description, url,
								writer, imageUrl);
					}
					if (writer.contains("VH1 Tuner")) {
						writer = "VH1 Tuner";
						fetch_VH1_Tuner(title, date, description, url, writer,
								imageUrl);
					}

				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			Message msg = mHandler.obtainMessage();
			Bundle b = new Bundle();
			b.putString("url", "");
			msg.setData(b);
			mHandler.sendMessage(msg);

		}

		private void fetch_MTV_News(String title, String date,
				String description, String url, String writer, String imageUrl) {
			// TODO Auto-generated method stub
			Elements content = null;
			try {
				org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
				content = doc.getElementsByClass("article-body");
				description = content.text();
				content = doc.getElementsByClass("thumb-lg");
				imageUrl = content.attr("src");

				News temp = new News(title, date, description, url, writer,
						imageUrl);
				List_News.add(temp);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		private void fetch_MTV_Buzzworthy(String title, String date,
				String description, String url, String writer, String imageUrl) {
			// TODO Auto-generated method stub
			Elements content = null;
			try {
				org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
				content = doc.getElementsByClass("entry");
				description = content.text();
				content = Jsoup.parse(content.toString()).getElementsByTag(
						"img");
				imageUrl = content.attr("src");

				News temp = new News(title, date, description, url, writer,
						imageUrl);
				List_News.add(temp);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		private void fetch_VH1_Tuner(String title, String date,
				String description, String url, String writer, String imageUrl) {
			// TODO Auto-generated method stub
			Elements content = null;
			try {
				org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
				content = doc.getElementsByClass("post_content");
				description = content.text();
				content = Jsoup.parse(content.toString()).getElementsByTag(
						"img");
				imageUrl = content.attr("src");

				News temp = new News(title, date, description, url, writer,
						imageUrl);
				List_News.add(temp);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

	public ArrayList<News> restore_News() throws IOException,
			ClassNotFoundException {
		FileInputStream fin = new FileInputStream(getCacheDir()
				.getAbsolutePath() + "/newsnews");
		ObjectInputStream in = new ObjectInputStream(fin);
		ArrayList<News> object = (ArrayList<News>) in.readObject();
		in.close();
		return object;
	}

	public void sauvegard_News(ArrayList<News> s) throws IOException {
		FileOutputStream fout = new FileOutputStream(getCacheDir()
				.getAbsolutePath() + "/newsnews");
		ObjectOutputStream out = new ObjectOutputStream(fout);

		out.writeObject(s);
		out.close();
	}

	public void CreateNewsList() {
		List_News = new ArrayList<News>();

		News temp = new News(
				"Busta Rhymes, Kanye West, Lil Wayne + Q-Tip's 'Thank You' Is A Grown-Ass Lesson In Not Giving A F*** (NSFW)",
				"November 7, 2013",
				"Busta Rhymes, Kanye West, Lil Wayne, and Q-Tip are beef-free in their new track,  Thank You.  If  you thought Busta Rhymes' recent ode to the twerk, aptly titled  #TwerkIt,  was his most NGAF record to date, then you're gonna want to pay close attention to Bussa Buss' latest single,  Thank You,  featuring Kanye West, Lil Wayne, and Q-Tip. How do we know this? Well, for starters,  Thank You  asserts once and for all that there is ZERO beef between G.O.O.D. Music and Young Money. Plus, thanks to important lines like  F*** it, let's get to drinking, poison our livers  and  Legendary swag flu and see the influence, see how we do it,  it seems pretty clear that Busta and his crew give absolutely no Fs about their health (but admittedly a few Fs about effortless swag). Listen to Busta Rhymes'  Thank You  featuring Kanye West, Lil Wayne, and Q-Tip after the jump. Over a chill-sounding throwback beat, Wayne casually spits:  Welcome to the bank, where you deposit Young Money and you get Cash Money.  Busta's up next to rhyme about dat party lyfe:  30 bottles, 20 waitresses, bring 'em over!  And Q-Tip? He's heavy into a discussion on demographics:  Ballerinas, Ballers and inbetweeners/ Blatant non-believers/and overachievers kickin' it.  There's room for everyone at this party! On the inspiration for this record, Busta candidly told MTV News it was all about a show of good faith between YMCMB AND G.O.O.D. music:  We did everything in our power to show the camaraderie, because it's been a lot of talk that over the last year or two about conflict between G.O.O.D. Music and Young Money and Cash Money,  he said.  I just wanted to put it to bed and create an eventful moment where me and Wayne being Young Money/Cash Money on one team, Q-Tip and Kanye be on another movement on G.O.O.D. Music; just showing that camaraderie and that alliance and just making it official on a real hip-hop level.  Moral of the story? YMCMB and Cash Money live harmoniously in a #NOBEEFZONE. Oh, and music brings everyone together. + Listen to Busta Rhymes'  Thank You  featuring Kanye West, Lil Wayne, and Q-Tip (NSFW). Photo credit: Cash Money Records/ GIF: gifphy Related Content Follow Buzzworthy On Twitter Tags New Song, Kanye West, Lil Wayne, Q-Tip, Busta Rhymes",
				"http://buzzworthy.mtv.com/2013/11/07/busta-rhymes-kanye-west-lil-wayne-q-tip-thank-you/",
				"MTV Buzzworthy",
				"http://buzzworthy.mtv.com//wp-content/uploads/buzz/2013/11/busta.png");
		List_News.add(temp);

		temp = new News(
				"Oh, What's That? You Want To See Lil Wayne Dressed Up As Fred Flintstone?? (PHOTOS)",
				"November 6, 2013",
				"Yabba-dabba-doo! To celebrate his son Dwayne III's birthday, Lil Wayne got his prehistoric on in a full head-to-cartoonishly-oversize-toe Fred Flintstone costume! Wow, like, literally call him Mr. Flintstone, 'cause he can make your bed kid's birthday party rock! And with that, we conclude the  Bedrock  references portion of this afternoon's post. Got it out of your system? Good. Moving on! Check out more Flintstone party photos, and watch a video after the jump. HBD from Lil Tunechi! Hmmm, according to the caption on this photo posted by Dwayne's mom, Atlanta radio personality Sarah ViVan, Weezy was singing  Happy Birthday  to his son. But we kinda have a sneaking suspicion that the  High School  rapper was ACTUALLY leading everyone in a round of a cappella  Let The Beat Build,  conductor-style. Either way, AWWWWWW. Mmmmmmmmmm, delicious fondant. OMG, Wayne even helped his son blow out the candles on the birthday cake! So adorable -- almost as adorable as the  Ha, real cute, CAN WE EAT THIS PLZ??  look on that kid in the middle's face. Oh, and in case you were wondering, it looks like this party was held at some kinda super fun Discovery Zone-meets-gymnasium venue filled with trampolines, foam pits, and more things that we're super distraught over not getting to play on. Invite us next year? Thx! + Watch Lil Wayne play some trampoline basketball with his son. Photo credit: Sarah ViVan's Instagram Tags photo, video, Lil Wayne",
				"http://buzzworthy.mtv.com/2013/11/06/lil-wayne-fred-flintstone-photos/",
				"MTV Buzzworthy",
				"http://buzzworthy.mtv.com//wp-content/uploads/buzz/2013/11/lil_wayne_flintstones_bday_1.jpg");
		List_News.add(temp);

		temp = new News(
				"Eminem And Chance The Rapper Are Tourmates, But Why Haven't They Met?",
				"November 1, 2013",
				"Chance the Rapper has been on the move since he dropped Acid Rap in April. He toured with Eminem and Macklemore overseas, recorded with Lil Wayne and sold out solo shows. So during a visit to  RapFix Live  this week, we asked the Chicago rapper to clear up a few things that have transpired since we last spoke to him: Did Kanye West really co-sign Acid Rap? Did Lil Wayne actually delay Dedication 5 just for Chance's feature? And what was it like meeting Eminem? Kanye West Co-Signs Acid Rap. Or Maybe Not. Back in July, Chance tweeted,  Kanye likes Acid Rap  and, of course, people took notice ... but maybe too quickly.  Lemme say on 'RapFix Live,' that was a lie,  Chance told Sway bluntly when the topic came up.  I have no idea how Kanye West feels about Acid Rap; I just tweeted that.  Seriously, I was making a statement. I just tweeted, 'Kanye likes Acid Rap,' and right after that there was a Complex story [saying], 'Look out for a Kanye and Chance collaboration!' And, I mean, that could happen, who knows? But I have no idea how Kanye feels about Acid Rap. I know a lot of people in Kanye's camp, I know Kanye has definitely heard Acid Rap, but I've never met Kanye.  We still don't know what to believe. Chance Toured With Eminem But Never Met Him. In August, Chance joined Kendrick Lamar and Odd Future to open for Eminem on his European tour dates, and he'll head to Australia with him again in February. There have been rumblings that Shady Records has been interested in the Chicago rapper for some time now, but if that's true, he wasn't ready to give up the details.  Eminem is one of my biggest influences in music, and going out to Europe with him the first time was just a great experience,  Chance told  RFL.   Some of my friends were out there — Tyler, Earl, Kendrick — so it was just a good experience because that was my first time in Europe and [it was] with people that have done it before and that are amazing artists.  What about Em though?  I've never met Eminem, you don't meet Eminem,  he said with a hint of sarcasm.  He has his own secret service.  Lil Wayne Delayed Dedication 5 For A Chance Feature Weezy Stans are not too enthusiastic when his mixtapes get delayed, but this time around they could've blamed it on Chance. He confirmed that Wayne actually did hold the release of Dedication 5 until the Chicago rapper finished recording and submitting  You Song.   I had just gotten back from Europe, doing the Eminem shows, and right when we got back in town I got a call from my manager and he told me that [YMCMB manager] Cortez Bryant said, 'Wayne wants a song for Dedication 5 and we got one day to give it to him,'   Chance recalled.  I saw on Twitter [Wayne] saying that he was gonna hold back the album, so I felt this immense amount of pressure on me to give him the hottest song I could give and with about an hour of thought I realized it only made sense to get my production team from Acid Rap — Nate Fox, Peter Cottontale, Cam from J.U.S.T.I.C.E. League — all in a room to make a song from scratch, and it came out to 'You Song.'   That's no small accomplishment for the 20-year-old rapper.  Yes, me and Wayne have a song together,  he said, taking it all in.  It's my favorite accomplishment and I say that with thought. It's the biggest accomplishment I've made to date. Because Lil Wayne is probably the biggest reason why I'm rapping and doing what I do.  Chance the Rapper is currently on the road, performing dates on his Social Experiment Tour.",
				"http://www.mtv.com/news/articles/1716673/eminem-kanye-west-chance-the-rapper-meeting.jhtml",
				"MTV News",
				"http://images1.mtv.com/uri/mgid:uma:content:mtv.com:1716673?width=281&height=211");
		List_News.add(temp);

		temp = new News(
				"Buzz Bites (10/24/13): Listen To Snippets Of The Wanted's 'Word Of Mouth' Before Its Official Release",
				"October 24, 2013",
				"Hear teasers from The Wanted's third studio album. + The Wanted's Word Of Mouth may not drop until Nov. 4, but in the meantime you can listen to snippets of each song prior to the album's release date. (Just Jared Jr.) + Watch behind-the-scenes footage from Beyoncé's 2014 calendar shoot, where the ever-flawless Bey strikes sultry poses in various stages of dress/ undress. (Idolator) + Check out video footage from Kanye West's proposal to Kim Kardashian from Monday night. The Kimye festivities went down at AT&T Park in San Francisco, where friends and family joined them for the romantic event. (MTV News) + Katy Perry is expected to score her second No. 1 album on the Billboard 200. Prism is predicted to sell around 275,000 copies by next week, which would make it Katy's biggest sales week ever. (Billboard) + Speaking of Katy, the cast of  Glee  were given the difficult task (more like mission impossible to us) of choosing to be team Katy or team Lady Gaga for their Nov. 7 show, which will also guest star Adam Lambert. Check out sneak preview stills from the episode. (MTV News) + Lil Wayne has teamed up with The Motivational Edge, which creates arts and educational programs to inspire youth. Weezy's selling everything from autographs to a featured slot for aspiring rappers to promote tracks on the Young Money website in an effort to raise $200,000 for the charity. (RapFix) Photo credit: Island Tags Buzz Bites, Katy Perry, Adam Lambert, Lady Gaga, Beyonce, The Wanted, Kanye West, Lil Wayne",
				"http://buzzworthy.mtv.com/2013/10/24/the-wanted-word-of-mouth-snippets/",
				"MTV Buzzworthy",
				"http://buzzworthy.mtv.com//wp-content/uploads/buzz/2013/10/wordofmouth.jpg");
		List_News.add(temp);

		temp = new News(
				"Paris Hilton's 'Good Time' Video: Lil Wayne, Shirtless Male Models, Mansion Ragers + And PARIS, B**** (NSFW)",
				"October 8, 2013",
				"Paris Hilton gives good face in her  Good Time  video. Let's just say that as secret admirers of guilty pleasure masterpiece  Stars Are Blind,  we felt that the wait for Paris Hilton's  Good Time  video was almost as excruciating as waiting for Britney Spears'  Work B****  video. Almost. Thankfully, Paris the Heiress (RHYMING!) has just unleashed the official  Good Time  video featuring Lil Wayne, and, dear friends, it was definitely worth the not one, not two, not three, but FOUR exhaustively painful teasers. Watch Paris Hilton's  Good Time  video featuring Lil Wayne after the jump. Dare we say this?  Good Time  is basically Miley Cyrus'  We Can't Stop  video minus all the weird french fry sculptures and, sadly (or happily?), twerking. In her brand-new clip, Paris throws a well-attended pool party at her mansion, and, in between taking some much-needed time to writhe around in a rhinestone-encrusted one-piece bathing suit (they say that's calming, no?), she splashes water on some hard-bodied male models, who seem to be the only party guests she cares about. #Priorities Next up is a verse from Paris' new YMCMB bestie, Lil Wayne, who has no qualms whatsoever with yelling out,  It's Paris Hilton, b**ch!  Together, Wayne and Paris head to da clurrrb (which we're pretty sure is just one floor of Paris' mansion) and ride out the rest of night twirling Bat Mitzvah-era glow sticks and, presumably, counting the many f***s they simply don't give. OK, considering how many pop diva videos we've seen over the years,  Good Time  isn't exactly raising the innovation bar. But are you really gonna say  nope!  to an artfully edited montage of Paris Hilton bouncing around and doing butt clenches in a deep-V one-piece while Weezy watches? Yeah, we didn't think so. + Watch Paris Hilton's  Good Time  video featuring Lil Wayne (NSFW). Photo credit: Cash Money Records Related Content Follow Buzzworthy On Twitter Tags video, Lil Wayne, Paris Hilton",
				"http://buzzworthy.mtv.com/2013/10/08/paris-hilton-good-time-video/",
				"MTV Buzzworthy",
				"http://buzzworthy.mtv.com//wp-content/uploads/buzz/2013/10/url.jpg.jpg");
		List_News.add(temp);

		temp = new News(
				"Paris Hilton Gets Wet With Lil Wayne In 'Good Time' Pool-Party Vid: Watch!",
				"October 7, 2013",
				"If there is one thing we learned from Paris Hilton's new music video,  Good Time,  is that the heiress certainly knows how to throw a party. The video, which premiered a day early on Rolling Stone, has Paris flaunting her figure in a barely there bikini as she throws an epic pool-party bash, complete with glow sticks, bottles and a 10-foot-tall robot, otherwise known as Kryoman.  The video is so sexy and so much fun, I really wanted it to be colorful and full of life and my friend Kryoman flew out to be in it and he just makes the video so much fun like spraying all the CO2 canisters,  Hilton recently told MTV News.  He is the party machine basically. I love him. I've watched him. He performs with all the biggest DJs around the world, like [David] Guetta, Avicii, Calvin Harris so it was really cool to have him in my video because I know everyone in the dance scene really knows him and just having him part of the party scene is really dope.  Not to be outdone by Kryoman, Cash Money labelmate Lil Wayne appears in the video, which was shot over a span of 24 hours. YMCMB president Birdman chills behind Weezy during his verse.  It was amazing, we had a great time,  Hilton said of shooting the video with Wayne and Birdman.  The set was so much fun, we had so many interesting things happening and I loved all the wardrobe, there were so many gorgeous outfits. It was just a party all day long.  So is the video a true depiction of what it's like to really party with Paris Hilton?  Definitely, it's just a fun party with beautiful people and having the time of our lives.  Thanks BO$$! Love you! So excited!  RT @BIRDMAN5STAR: Congrats @ParisHilton Video and song is Amazin. Goodtimes YMCMB RichgangRichgirl— Paris Hilton (@ParisHilton) October 7, 2013  Good Time,  produced by Afrojack, is the lead single off her upcoming 2014 album and marks Hilton's return to music after a seven-year hiatus.  I've been recording this album for about a year and a half now,  Hilton revealed.  After being in Ibiza I was so inspired I met so many amazing DJs and producers, so people kept sending me different songs and making this album and I'm so proud of it. ",
				"http://www.mtv.com/news/articles/1715206/paris-hilton-gets-wet-with-lil-wayne-good-time-pool-party-vid-watch.jhtml",
				"MTV News",
				"http://images4.mtv.com/uri/mgid:uma:content:mtv.com:1715206?width=281&height=211");
		List_News.add(temp);

		temp = new News(
				"Paris Hilton Drops 'Good Time' Featuring Lil Wayne, We Drop Dead From Anticipatory Exhaustion",
				"October 7, 2013",
				"Are you having a good time? Asking for a friend. (Paris Hilton) The bad news is that Paris Hilton's  Good Time  video featuring Lil Wayne still won't come out until tomorrow, Oct. 8. The good news is that we've at least got our hands on the audio for her comeback single, an ode to foam, diamonds, and being 'bout that Ibiza lyfe. The practical news is that we've remembered to drink water at regular 90-minute intervals throughout the day. The bratty news is that we wanna see the  Good Time  video NOW. Listen to Paris Hilton's  Good Time  featuring Lil Wayne after the jump. So, what's new this time around the ol' Heiress Paris  Good Time  merry-go-round? (#BadAtMetaphors) Well, we finally hear the high-energy verses and dance breaks in between the  Are ya havin' a good time?  hook that we've already heard in the teasers. We can definitely imagine ourselves letting loose in the middle of the Amnesia Nightclub dance floor with our glamorous, jet-setting entourage. You can't meet them. They live in Canada. Fellow Cash Money label mate Lil Wayne also gets to unleash his full verse this time. It doesn't go any deeper than the  I walked up to a big butt/ And asked her ass, 'But, what?'  lyric we already listened to, but hey, isn't thinking too hard kinda antithetical to having a good time? (Remember: Don't think too hard on your answer.) + Listen to Paris Hilton's  Good Time  featuring Lil Wayne. Photo credit: Cash Money Records Tags New Song, Lil Wayne, Paris Hilton",
				"http://buzzworthy.mtv.com/2013/10/07/paris-hilton-lil-wayne-good-time/",
				"MTV Buzzworthy",
				"http://buzzworthy.mtv.com//wp-content/uploads/buzz/2013/10/paris_hilton_good_time_hero.jpg");
		List_News.add(temp);

		temp = new News(
				"Paris Hilton Is Ready To Have 'Good Time' On New Album",
				"October 3, 2013",
				"[ video unavailable on this device ] It’s been seven years since Paris Hilton first released “Stars Are Blind” and now the singer is back! After signing with Cash Money Records in May, Hilton returned to the studio to work on her forthcoming album, which she says is all about having a good time. “It’s music you want to party to,” Hilton says. “Just really happy, upbeat music.” Hilton also explains how her new label, which is home to Lil Wayne and Nicki Minaj, influenced her sound. Her first album had a pop feel and this time she utilized house and hip hop beats, giving her new album an urban edge. But what about her first single, “Good Time”? “I think it’s the perfect first single,” Hilton says. [ video unavailable on this device ] While the song doesn’t officially hit the airwaves until October 8th, it hasn’t stopped the singer from teasing the new video. “I wanted the video to be a really amazing pool party,” Hilton explains. “It’s wild; they’re beautiful people.” One of them being Hilton herself, who looks flawless in the teaser she released earlier this week. And let’s not forget Lil Wayne makes an appearance on the track! – [Photo: Getty; GIF: Stacy Lambe/VH1]",
				"http://www.vh1.com/music/tuner/2013-10-03/paris-hilton-to-have-good-time/",
				"VH1 Tuner",
				"http://vh1.mtvnimages.com/uri/mgid:uma:video:vh1.com:961015?width=615&height=345");
		List_News.add(temp);

		temp = new News(
				"Paris Hilton's New 'Good Time' Teaser: Blacklight Luxury, Sentient Fog Machines + A Snippet Of Lil Wayne's Verse (VIDEO)",
				"October 2, 2013",
				"We are so close to having a  Good Time  with Paris, y'all. Bad news first: As you may have noticed, Oct. 1 came and went without Paris Hilton's promised  Good Time  video featuring Lil Wayne, which has been pushed back until next Tuesday, Oct. 8. Stop crying -- if you need to cry, kindly show yourself to the two-story Spanish-style doggie mansion out back. Now, on to the good news! Out of the goodness of her rhinestone-bedazzled, possibly  Hello Kitty -shaped heart, Paris has not only blessed us with this truly kaleidoscopic  Pretty Pretty Princess  single cover art showcased above, but an extended 43-second teaser as well. Our thoughts? Let's just say that it starts with an  F  and ends in  LAWLESS.  Give up? FLAWLESS. Watch Paris Hilton's extended  Good Time  teaser video featuring Lil Wayne after the jump. Picking up right where the original sneak peek clip left off, the teaser shows Paris, Weezy, and her Cash Money CEO Birdman living it up at the greatest water- and/or vodka-drenched pool party ever -- complete with blow-up giraffe floaties! (Why is that the thing that impresses us??) In her YMCMB ( Young Money Cash Money Billionaires ) black snapback and aptly branded  Rich Girl  tank, Paris plays with some seriously next-level glow sticks, dances with fog-spewing robots, and builds a strong visual case for the inclusion of a  Good Time  filter on Instagram. All the while, she coos,  Are you havin' a good time?  To that we say: Uh, duh. This time around, we even get to hear a snippet of Lil Wayne's guest verse:  I'm f***** up/ I can't tell you what's what/ All she know is s*** f***/ I walked up to a big butt/ And asked her ass,  But, what?  Full disclosure: We have no idea what he's talking about in these vaguely Seuss-ical lyrics, but we are LIVING for the super literal mental picture they conjure. So, that's that! Six more days to go until Paris ends the last 1,000 years of  Bad Time  that the universe has been forced to endure. Honestly, we're kinda glad that the  Good Time  video didn't drop yesterday, i.e., the same day as Britney Spears'  Work Bitch.  That would've been, like, a lethal combination of pop princess flawlessness. Not even the po-lice or the gov-ah-nah could have saved us! + Watch PARIS HILTON'S EXTENDED  GOOD TIME  TEASER VIDEO FEATURING LIL WAYNE. Not satisfied, hungry tigers? Watch PARIS HILTON DISCUSS  GOOD TIME  and HER NEW ALBUM. [ video unavailable on this device ] [ video unavailable on this device ] Photo credit: Cash Money Records Tags video, Lil Wayne, Paris Hilton",
				"http://buzzworthy.mtv.com/2013/10/02/paris-hilton-lil-wayne-good-time-extended-teaser-video/",
				"MTV Buzzworthy",
				"http://buzzworthy.mtv.com//wp-content/uploads/buzz/2013/10/paris_hilton_good_time_hero.jpg");
		List_News.add(temp);

		temp = new News(
				"Paris Hilton And Lil Wayne Are 'A Bit Tipsy': See Their 'Good Time' Teaser!",
				"October 2, 2013",
				"If there's one thing Paris Hilton has proved over and over it's that she knows how to have a good time. A really good time. She proves it again in the 43-second teaser clip for her Cash Money Records debut single,  Good Time.  Fittingly for the hotel heiress whose jet-set lifestyle often finds her lounging around pools in designer weekend gear, the swimwear-heavy clip finds her at the center of a kind of poolside rave, complete with giant, fog-spewing light-up robots, bottle service, blow-up animals and plenty of shirtless hunks waving glow sticks all around her. She promised it would be  so sexy and so much fun  and colorful, and, well, she wasn't kidding. Rocking her YMCMB snapback and an array of expensive-looking bathing suits, Hilton sings the songs breathy refrain,  Are you havin' a good time/ 'Cause I'm having a good time/ I might be a bit tipsy/ but that's OK 'cause you're with me,  with the last line shifting her voice into AutoTune mode. Rocking a soaking wet  RichGirl  tank, bedazzled one-piece and leopard print bikini, Hilton mostly does what she does best: gaze into the camera dreamily and invite you to the non-stop party with her drowsy eyes. Her musical mentor, Birdman, makes a cameo, as does labelmate Lil Wayne, who drops an appropriately horny verse as the song's EDM beats and spiky keyboards kick in.  I'm f---ed up/I can't tell you what's what/All she know is suck, f---/I walked up to a big butt/And asked her ass but what?/Tunechi never slacks without her button up.  Hilton, who geared up for her Cash Money debut with a month-long DJ residency at Ibiza hotspot Amnesia, told MTV News last month that she's ready to prove her musical chops.  Music has always been a big passion of mine, but I've just been so busy with everything else. But I've recorded some incredible songs, EDM-influenced, electro-pop, and working with very talented producers,  Hilton said.  I'm really happy to be a part of the Cash Money family.  The full version of the song is due out on iTunes on October 8.",
				"http://www.mtv.com/news/articles/1714916/paris-hilton-lil-wayne-bit-tipsy-see-their-good-time-teaser.jhtml",
				"MTV News",
				"http://images3.mtv.com/uri/mgid:uma:content:mtv.com:1714916?width=281&height=211");
		List_News.add(temp);

		temp = new News(
				"Which Recurring Musical Franchise Is The Most Successful?",
				"October 1, 2013",
				"If you’re asking yourself, “Hey, didn’t Justin Timberlake already release an album earlier this year?”, the answer is yes, yes he did. The 20/20 Experience came out in March and, thanks to pent-up demand for new Timbertunes, has sold 2.279MM copies, making it the best-selling LP of the year so far. Apparently, our pal JT abides by the ole “Strike while the iron is hot” philosophy, which is why you see that the The 20/20 Experience: Part II was just released yesterday. Which got us thinking: How have OTHER sequel albums released this century performed? The folks who churn out big budget Hollywood movies have long since understood the power of sequels. Since one has already spent considerable energy establishing things like a brand and a concept that audiences have responded positively to, it makes economic sense not to always start from scratch when considering your next project. In other words, if it ain’t broke, don’t fix it! Well, as well all know, the movie business and the music business are considerably different animals. While sequels have a solid track record at the multiplex, their success is less certain when it comes to recorded music. So, on the day that JT releases his first official sequel, we thought we’d take a look back at how other musical sequels—from the likes of Jay Z, Lil Wayne, Timbaland and more—have performed so we’ll know how to gauge the success (or failure?) of The 20/20 Experience: Part II. So, moving from least successful to most successful, here goes nothing… 8. Gym Class Heroes, The Papercut Chronicles I and II FIRST RELEASE: The Papercut Chronicles (2005) METACRITIC SCORE: N/A SALES TO DATE*: 32,000 copies SECOND RELEASE: The Papercut Chronicles II (2011) METACRITIC SCORE: 53 SALES TO DATE*: 63,000 copies ANALYSIS: Gym Class Heroes aren’t really what you’d call an “album” band. Like a lot of other acts of their generation, they live and die by their (at times very popular!) singles. Their 2005 release of The Papercut Chronicles isn’t widely viewed as an important or influential work, but it did win them a decent-sized fanbase (including the members of Fall Out Boy). After a few middling works and Travie McCoy’s ultimately unsuccessful bid to go solo, the band churned out a second volume of The Papercut Chronicles in 2011, which featured the biggest hit of the group’s career to date: “Stereo Hearts (feat. Adam Levine).”",
				"http://www.vh1.com/music/tuner/2013-10-01/musical-sequels/",
				"VH1 Tuner",
				"http://musicblog.vh1.com/wp-content/uploads/2013/09/musicfranchises.jpg");
		List_News.add(temp);

		temp = new News(
				"Drake's Nothing Was The Same: 10 Subliminal Shots And Shout-Outs We Can't Ignore",
				"September 25, 2013",
				"Drake's brand-new Nothing Was the Same is not only musically rich, it's lyrically dense. And we know that OVO diehards have been hitting the rewind button, catching quotables and clever one-liners. But there are quite a few rappers that should probably be taking second- and even third listens to Drake's latest album too. Some receive praise and adoration, while others may have gotten the subliminal end of the dis stick. Let's see who's who: Prodigy: Sometimes it feels like the Mobb Deep MC doesn't get enough credit for delivering some of the best murder music ever. Drake has clearly been influenced though. On the intro to NWTS, he borrows bars from Prodigy's first solo single,  Keep It Thoro.   Heavy airplay all day, with no chorus, we keep it thorough, n---a/ Rap like this for all of my borough, n---as,  Drizzy spits as if he was flowing over an Alchemist beat. Pusha T: The VA rapper and Lil Wayne have had something of a cold war going on, and that's been documented. But Pusha's never been the biggest Drake fan either: Who can forget  the swag don't match the sweaters  line from his  Don't F--- With Me  freestyle? It's hard to tell with all of the subliminals being flung back and forth, but a few of Drizzy's  Tuscan Leather  bars could have been aimed at the G.O.O.D. Music MC.  I'm just as famous as my mentor, but that's still the boss, don't get sent for/ Get hyper on tracks and jump in front of a bullet you wasn't meant for,  Drizzy rhymes before delivering a few more jabs.  Here's a reason for n---as that's hatin' without one/ That always let their mouth run/ Bench n---as talkin' like starters, I hate it/ Started from the bottom, now we here, n---a, we made it.  Nicki Minaj: How much more time are we going to spend on the intro? Well, it's not our fault Drake gave us so much to digest on the opening track. By this time we're sure Nicki has heard the lyric:  Not even talkin' to Nicki, communication is breakin'/ I dropped the ball on some personal sh--, I need to embrace it.  Jay Z And Every Other Rapper Who Thinks They're The G.O.A.T.: Every rapper should feel like he's the best right? So why should Drake be any different? On his third album (fourth if you count So Far Gone as an album), Aubrey lets everyone know he's no longer the little homey.  Like I should fall in line, like I should alert n---as/ When I'm 'bout to drop something crazy and say I'm not the greatest,  he moans on  Paris Morton Music 2.  Kendrick Lamar: Drake isn't into naming names. (Remember when he went head-to-head with Common on Rick Ross'  Stay Schemin'  ? His response was stern but subtle.) But there's no way the nimble lyricist was going to let Kendrick Lamar's  Control  verse go unchecked, right?  F--- any n---a that's talkin' that sh-- just to get a reaction/ F--- going platinum, I looked at my wrist and it's already platinum,  Drizzy spits on  The Language,  a possible reference to K-Dot's platinum-selling good kid, m.A.A.d city. Bun B and Paul Wall: Drake's love for Houston is well documented. On  Too Much,  he reminisces about H-Town and waiting backstage at the famed Warehouse for Bun B to show up:  Met a lot of girls in my time there, word to Paul Wall, not one fronted.  Ma$e: In December, the Harlem rap vet told MTV News the only artists he would ever sign to now was Kanye West or Drake. We haven't heard of any deals, but Drizzy must have heard the message loud and clear. Not only did he invite M-A-Dollar Sign to his OVO Fest, he paid homage to the former Bad Boy's  Mo' Money, Mo' Problems  verse, on  Worst Behavior.  Who's hot? Who's not? Wu-Tang Clan: Some Wu-Fanatics were outraged when Drake drew comparisons to the almighty Clan on  Wu-Tang Forever,  but the odes to Shaolin's finest don't end on track #4. The Toronto MC listens to Cappadonna on  Tuscan Leather ; shouts out O.D.B. on  Worst Behaviour ; and makes way for Timbaland to spit the hook to Wu's 1993 classic  C.R.E.A.M  on  Pound Cake.  Lil Wayne: Turns out Drake and Weezy had already crossed paths before the Young Money boss flew him out to Houston to join him on tour in 2008. Drizzy recalls their first a run-in on  Paris Morton Music 2.   He walked right past in the hallway/ Three months later, I'm his artist,  Drake reveals, though he speculates Tunechi probably doesn't recall it. Birdman: Rumors of Drake jumping off the YMCMB ship are a regular thing and they always get fans talking. While the NWTS star has always denied those reports, he once again puts the rumors to rest.  They know I come right every summer/ Cash Money Records forever, I'm always Big-Tyming, bitch/ I came up right under Stunna,  he spits on  The Language. ",
				"http://www.mtv.com/news/articles/1714591/drakes-nothing-was-same-10-subliminal-shots-shout-outs-we-cant-ignore.jhtml",
				"MTV News",
				"http://images2.mtv.com/uri/mgid:uma:content:mtv.com:1714591?width=281&height=211");
		List_News.add(temp);

		temp = new News(
				"Lil Wayne Chats Up Katie Couric About Retirement",
				"September 9, 2013",
				"Lil Wayne has a special relationship with Katie Couric. Back in 2009, the pair hung out, bowled and talked about what it means to be a gangsta. The unlikely friendship continued on Monday (September 9), when Wayne (who Couric called  hip-hop's mad genius ) sat down with the former network news anchor on her syndicated  Katie  talk show to discuss why he dropped out of high school, his dangerous addiction to syrup and why he's really, really ready to call it a day on his rap career. Since their last talk, Wayne did a stretch in prison on a gun possession charge, so naturally Couric was curious how the other prisoners treated the rapper.  It's jail,  he answered when asked if the other convicts were excited to see such a famous face.  So, for the first 20-30 seconds you're like, 'Oh my God that's really him,' then ... the reality of where you're at clicks right back in.  Couric, or  Miss Katie,  as Wayne referred to her the first time around,also asked about how Weezy's children are doing, which caused the Young Money boss to shake his head and blush a bit.  My oldest is my beautiful daughter, she's 14, Reginae. She's awesome, she's a firecracker,  said Wayne, who dressed conservatively in a white t-shirt, khakis and a  trippy  baseball hat.  Personality out of this world.  He called his three sons his  everything  and reiterated that he wants to retire in five years, when he turns 35.  Whatever they want to do,  he said about his plans to spend time with them when he hangs up the mic.  I don't want to have no influence ... 'Hey you must do this, you must do that.' Because I didn't have that and because I didn't have that I grew up to be exactly what I wanted to be and how I wanted to be. I want that for all of my kids ... I know I'll be ready to retire at 35 because I am so ready to retire now!  The admittedly  odd couple  shared a laugh over the  I'm a gangsta, Miss Katie  line that Wayne dropped the first time around, which brought a smile to Couric's face on the first show of her second season. Asked if he regretted not finishing high school, Wayne, an honor student, drew a laugh from the studio audience when he said,  Not at all ... I mean, kids, please finish high school. And all my kids, you are finishing high school!  Getting serious, Weezy explained that it was actually his mother's idea for him to drop out. At the time he quit, Wayne already had a platinum album out and one day when she saw him getting ready to go to his public high school, his mother saw him getting ready for the day, which included packing his gun in his backpack.  You gotta bring that to school with you?  she asked him.  You don't want me to bring it? he replied.  I do,  she responded after thinking about the weapon she'd bought him for protection. Worried that he had to go to school armed, she counseled him to get his GED and then go to college instead. Admitting that he's an  excessive  person who tends to do things to the extreme, Wayne also opened up about his abuse of syrup. Doctors thought the series of seizures he suffered earlier this year were a result of dehydration, lack of sleep, working too hard and what they thought was newly-discovered epilepsy. Wayne's mother, however, revealed that when he was younger he  fainted  at a public park, which she now realizes might have been a seizure. While looking for a source of the seizures, Wayne told his physicians that he'd cut way back on syrup, but that he'd been taking pain pills that also had codeine in them. Asked if he's still sipping, Wayne said,  No, I can't.  And when Couric wondered what made him stop, he said,  My doctors. I was doing it too much.  While his doctors couldn't tell him what to do, they suspected his mother could have an influence on his drug abuse issues.  I suggest that your mom tell you to stop,  Weezy said the doctors told him, after discovering that one of his cousins had epilepsy and that another had died of the disorder. And, sure enough, the rapper's mother did tell him to cut it out and he, of course, listened. In addition to giving the Trukfit boss his own custom  Katie  branded skateboard, Couric also had her 2005 minivan pimped in Wayne's honor for the show.  You got sheepskin!  Wayne laughed when he heard about the interior of the car she's giving away to one of his fans.",
				"http://www.mtv.com/news/articles/1713756/lil-wayne-chats-up-katie-couric-about-retirement.jhtml",
				"MTV News",
				"http://images4.mtv.com/uri/mgid:uma:content:mtv.com:1713756?width=281&height=211");
		List_News.add(temp);

		temp = new News(
				"Rich Gang's 'We Been On' Video: Watch R. Kelly, Birdman, And Lil Wayne Throw A Massive Rager In An Abandoned Mansion (NSFW)",
				"September 5, 2013",
				"Lil Wayne, Birdman, and R. Kelly are clearly 'BOUT THAT LIFE. We've said it before, and we'll say it again: If we could spend our days popping bottles in empty mansions with gorgeous women and/or dudes serving us organic grapes, we LITERALLY MIGHT. And that's not to say we wouldn't simultaneously do our best to obtain a liberal arts degree from a top private university ('welcome, Mom!) -- it's just that Rich Gang, R. Kelly, Lil Wayne, and Birdman seem to be having the best time doing all of the above in their  We Been On  video (minus the liberal arts university). Who wouldn't want in?? Watch Rich Gang's  We Been On  video featuring R. Kelly, Lil Wayne, and Birdman after the jump. In the Hannah Lux Davis-directed clip, which is taken from Rich Gang's self-titled compilation album, R. Kelly, Birdman, and Lil Wayne throw a balls out rager in an gilded mansion. (Maybe the same one they partied in during their  Tapout  video?) Over the course of the party, all three rappers get served top-shelf bevvies by superhot chicks (natch), and as they happily jam out at the best fortress house party you've never been invited to, each bro takes turns singing about being so flush, even two American Express Black Cards won't cut it:  Nothin' but Cigars/ Audemars/ Got too many foreigns you need a passport to walk in my garage/ Take a lifetime to spend money this long.  OK, seriously, how can we get to a place where we're so loaded, our literal day job is to figure out ways to spend our money? I mean,  We Been On 's concept isn't the most original in the rap game, but then again, we might spend our days singing about  Breaking Bad -piles of cash too if our bank accounts could handle purchasing the entire new 2013 Spring Ready-to-Wear Valentino collection. Or a new car wash. Either one! + Watch Rich Gang's  We Been On  video featuring R. Kelly, Lil Wayne, and Birdman (NSFW). Photo credit: Young Money Related Content Follow Buzzworthy On Twitter Tags video, Lil Wayne, R. Kelly, Birdman, Rich Gang",
				"http://buzzworthy.mtv.com/2013/09/05/rich-gang-r-kelly-birdman-lil-wayne-we-been-on-video/",
				"MTV Buzzworthy",
				"http://buzzworthy.mtv.com//wp-content/uploads/buzz/2013/09/Screen-shot-2013-09-05-at-8.52.15-AM.png");
		List_News.add(temp);

		temp = new News(
				"Buzz Bites (9/3/13): *NSYNC's Lance Bass Is Engaged!",
				"September 3, 2013",
				"Lance and Michael are getting married! + Congratulations to Lance Bass and boyfriend Michael Turchin, who are now engaged!  He said YES!!  the former *NSYNC member shared on Instagram following his reported New Orleans proposal, along with a photo of the happy couple. (Billboard) + Lady Gaga premiered new ARTPOP material during her seven-song iTunes Festival set in London on Sunday, including  Aura,   Sex Dreams,  and  Jewels & Drugs  featuring T.I., Too $hort, and Twista. (MTV News) + Stream The Weeknd's forthcoming album, Kiss Land, in its entirety before its Sept. 10 release. FYI, his official debut solo album (last year's The Trilogy combined three previously released mixtapes) features a guest spot from Drake. (NPR) + Download Lil Wayne's Dedication 5, the fifth installment of his mixtape series. The 29-song (!) track list includes collabs with The Weeknd, 2 Chainz, T.I., and Chance The Rapper. (Rap-Up) + It looks like  American Idol  has secured its judges for season 13! Harry Connick, Jr. will join Jennifer Lopez and Keith Urban behind the panel. (VH1) + Sleigh Bells have announced that their upcoming third album, Bitter Rivals, will drop Oct. 8. Check out the video for the disc's title track, its 10-song track list, and the band's fall tour schedule. (Pitchfork) Photo credit: Lance Bass' Instagram Related Content Follow Buzzworthy On Twitter Tags Buzz Bites, Lady Gaga, Jennifer Lopez, Drake, Keith Urban, Lil Wayne, The Weeknd, 2 Chainz, T.I., Too Short, *NSYNC, Lance Bass, Chance the Rapper, Twista, Harry Connick Jr., Sleigh Bells",
				"http://buzzworthy.mtv.com/2013/09/03/nsync-lance-bass-engaged-photo/",
				"MTV Buzzworthy",
				"http://buzzworthy.mtv.com//wp-content/uploads/buzz/2013/09/LanceBassEngaged.jpg");
		List_News.add(temp);

		temp = new News(
				"First Dibs: Is Harry Connick Jr. A New American Idol Judge?",
				"September 1, 2013",
				"Did a chipmunk attack Carrie Underwood‘s husband? Is Lil Wayne‘s new mixtape finally out? What was the last prank the boys of One Direction played on one another? Harry Connick Jr. is joining American Idol as a judge. The announcement is good news for fans who noticed that Connick actually gave honest and amusingly biting critique to the contestants when he guest mentored last season. [Rolling Stone] A chipmunk got into Carrie Underwood’s house and it bit her husband, Mike Fisher. Underwood posted a Vine clip where she asks Fisher to save the chipmunk before it bites him and not to hurt it. [People] After a brief delay, Lil Wayne’s new mix tape, Dedication 5, is officially out. The tape features 29 tracks and appearances by T.I., 2 Chainz and more. [Rap-Up] One Direction’s tour documentary, This Is Us, is in movie theaters this weekend and the film shows a softer side of the band–as well as their prankster side. The guys chatted with MTV News about their favorite songs and the last pranks they pulled on one another. [MTV News] [ video unavailable on this device ] [Photo Credit: Getty Images]",
				"http://www.vh1.com/music/tuner/2013-09-01/first-dibs-harry-connick-jr-american-idol/",
				"VH1 Tuner",
				"http://musicblog.vh1.com/wp-content/uploads/2013/09/firstdibst901.jpg");
		List_News.add(temp);

		temp = new News(
				"First Dibs: Does Paul McCartney's  New  Music Sound Old?",
				"August 31, 2013",
				"Is Lady Gaga‘s new song “Swine” more rock than pop? Why was Lil Wayne‘s mixtape pushed back? Is Drake worried about Kendrick Lamar? Paul McCartney is back with a new song appropriately called, “New.” The song that sounds like a classic McCartney cut was produced by Mark Ronson. [Rolling Stone] Lady Gaga teased her new single, “Swine,” in a video promoting the upcoming iTunes festival. The song has a decidedly more rock vibe than “Applause.” [FishWrapper.com] Lil Wayne’s new mix tape, Dedication 5, was supposed to drop yesterday, but it’s been pushed back. Wayne is tweaking some things and adding an additional track. [Rap-Up.com] Drake is more excited about the album art for Nothing Was The Same than he is worried about Kendrick Lamar’s threats on Big Sean‘s “Control.” “I know well and good that Kendrick’s not murdering me,” Drake said. [MTV News] [ video unavailable on this device ] [Photo Credit: Getty Images]",
				"http://www.vh1.com/music/tuner/2013-08-31/first-dibs-paul-mccartney-new/",
				"VH1 Tuner",
				"http://musicblog.vh1.com/wp-content/uploads/2013/07/firstdibs727.jpg");
		List_News.add(temp);

		temp = new News(
				"Drake Relives Lil Wayne's 'Surreal' 2008 VMA Shout-Out",
				"August 25, 2013",
				"BROOKLYN, New York — Drake first appeared on the MTV Video Music Awards in 2010, but before he made his debut alongside Mary J. Blige and Swizz Beatz, the Toronto spitter made his presence felt a few years earlier thanks to his mentor Lil Wayne. When Weezy took the stage at the 2008 VMAs to perform Tha Carter III's  Misunderstood,  the Young Money boss left his own lyrics at home, opting to spit bars written and recorded by Drake, who was largely unknown as a rapper at the time. At the end of the performance, he yelled out  Drizzy Drake, I love you, boy!  It was the ultimate co-sign.  It was like surreal; I think I was locked outside. I couldn't even get inside, I was at a side door and I heard it,  Drake recalled.  The door kept opening and closing and I kinda heard lyrics from my song and I thought like is he rehearsing and then people kept hitting my phone like Wayne is performing. That was crazy.  The verse that Wayne performed would eventually end up on Birdman's 2009 platinum single  Money to Blow.  That 2008 night helped to set up Drizzy as an artist to watch for. Drake would make his physical VMA debut in 2010 to put on a Rat Pack-inspired performance of  Fancy  with MJB and Swizz.  I just really remember like how nervous I was at my first VMAs,  he said.  It's one thing to be comfortable with yourself, be yourself in everyday life, but it's hard to walk into this building and still be charismatic, still be personable and still be an individual with presence because there are so many big people here.  Tonight, the Young Angel will hit the 2013 VMA stage at the Barclays Center in Brooklyn to perform  Started from the Bottom  and his latest Nothing Was the Same single  Hold On, We're Going Home.   We're blowing the stage up, I got things going off, I had to test with the fire marshall and it's gonna be good,  he told us on Friday night.  I've been doing this for a while, pyro doesn't scare me. You know I'm ready, if you've ever been onstage with Lil Wayne, he likes explosions and they're loud you know, so I'm used to it. ",
				"http://www.mtv.com/news/articles/1712994/drake-relives-lil-waynes-surreal-2008-vma-shout-out.jhtml",
				"MTV News",
				"http://images3.mtv.com/uri/mgid:uma:content:mtv.com:1712994?width=281&height=211");
		List_News.add(temp);

		temp = new News(
				"Star Spotting: Nicki Minaj, Lil Wayne, Birdman + Mack Maine Put Your Sears Family Portrait To SHAME (PHOTO)",
				"August 23, 2013",
				"It was a Cash Money family affair at last night's BMI R&B/Hip-Hop Awards Wanna know reason No. 37,405 why pop stars are seriously no #JustLikeUs? Whereas normal people's family photos usually consist of us looking all awk in front of European monuments whose histories we're TBH a little #unclear on, a celeb like Nicki Minaj's Kodak moments depict the Maybach twerker with the flyest manicure on the block looking flawless while casually seated next to her Cash Money  family,  aka Nicki's  High School  collaborator Lil Wayne, Birdman, and Mack Maine. So yeah, just about the polar opposite of me and my parents standing in front of the Berlin Wall. After racking up a ton of awards, the  Love More  rapper took a moment pose with her  fam,  and naturally Instagrammed said moment for posterity, captioning:  #bmirnbhiphopawards like 7 awards tonite i think. I llost count. And cash money is being honored. Thx bmi.  Oh, just a measly seven trophies, sitting next to my super famous crew, NBD -- shyeah right! P.S., don't think we weren't gonna comment on that perfect tights game you've got going on, Nicki. To quote your Busta Rhymes collab: #TWERKIT. Photo credit: Nicki Minaj's Instagram Related Content Follow Buzzworthy On Twitter Tags Star Spotting, Lil Wayne, Nicki Minaj, Birdman, Mack Maine",
				"http://buzzworthy.mtv.com/2013/08/23/nicki-minaj-lil-wayne-birdman-mack-maine-bmi-awards-photo/",
				"MTV Buzzworthy",
				"http://buzzworthy.mtv.com//wp-content/uploads/buzz/2013/08/Screen-shot-2013-08-23-at-9.06.21-AM.png");
		List_News.add(temp);

		temp = new News(
				"Buzz Bites (8/21/13): Britney Spears Teases Her Sia Collaboration On Twitter",
				"August 21, 2013",
				"We can't wait to hear Brit's upcoming collab with Sia! + Britney Spears is getting us PUMPED for her upcoming eighth studio album. First, she shared a vague  Album 8 Recipe  visual on Facebook, and then Brit installed a countdown clock for her  All Eyes On Me  single on her site. Now, she's revealed a collab with Sia (who wrote Rihanna's smash hit  Diamonds ), tweeting,  One of the most beautiful songs I have recorded in a LONG time. (Idolator) + Taylor Swift gave tourmate Ed Sheeran jars of homemade jam, one of which bore a not-so-subtle Kanye West reference on its label:  Yo Ed, I'm really happy 4 you and I'm gonna let you finish but this is the best JAM OF ALL TIME.  (MTV News) + Speaking of Ed Sheeran, the adorbz ginger has joined the cast of  The Voice  as a mentor for Christina Aguilera's team, and he's headlining his own show at Madison Square Garden on Nov. 1.  My dad always said you haven't made it til you've played MSG so it's a massive honour,  he tweeted. (E!) + Watch the full-length commercial for Katy Perry's new fragrance, Killer Queen. Not to give too much away, but she's pretty dynamite with a laser beam -- metaphorically speaking. Bonus: Check out some behind-the-scenes photos from the set. (MTV Style) + Hear Juicy J's  The Woods  featuring Justin Timberlake from the rapper's Stay Trippy, which comes out on Aug. 27. Other featured artists on the album will include Lil Wayne, A$AP Rocky, 2 Chainz, and more. (Rap-Up) + Listen to M.I.A.'s  Unbreak My Mixtape,  which features a sample from Blur's  Tender.  The Brit's eagerly anticipated fourth studio album, Matangi, drops on Nov. 5. (Pitchfork) Photo credit: Getty Images Related Content Follow Buzzworthy on Twitter Tags Buzz Bites, Katy Perry, Rihanna, Taylor Swift, Justin Timberlake, Britney Spears, M.I.A., Ed Sheeran, Juicy J, Kanye West, A$AP Rocky, Lil Wayne, Sia, 2 Chainz, Blur, Christina Aguilera",
				"http://buzzworthy.mtv.com/2013/08/21/britney-spears-sia-collaboration-announcement/",
				"MTV Buzzworthy",
				"http://buzzworthy.mtv.com//wp-content/uploads/buzz/2013/08/britney_hero.jpg");
		List_News.add(temp);

	}

}
