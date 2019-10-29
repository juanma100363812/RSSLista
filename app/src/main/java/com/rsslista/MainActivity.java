package com.rsslista;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listview;
    URL url = null;
    RssFeed feed = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = (ListView) findViewById(R.id.lvRSS);

        try {
            url = new URL(
                    "https://news.yahoo.com/rss/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new ReadRssTask().execute(url);
    }

    private class ReadRssTask extends AsyncTask<URL, Void, RssFeed> {

        @Override
        protected RssFeed doInBackground(URL... params) {
            RssFeed result = null;
            URL url = params[0];
            if (!TextUtils.isEmpty(url.toString())) {
                try {
                    result = RssReader.read(url);
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        protected void onPostExecute(RssFeed result) {
            if (result != null) {
                ArrayList<RssItem> rssItems = (ArrayList<RssItem>) result
                        .getRssItems();
                List<String> titles = new ArrayList<String>();
                for (RssItem rssItem : rssItems) {
                    titles.add(rssItem.getTitle());
                }
                ArrayAdapter<String> list = new ArrayAdapter<String>(
                        getBaseContext(), android.R.layout.simple_list_item_1,
                        titles);
                listview.setAdapter(list);
            }
        }

    }
}
