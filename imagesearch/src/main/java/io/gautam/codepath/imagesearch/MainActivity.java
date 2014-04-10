package io.gautam.codepath.imagesearch;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final int SEARCH_SETTINGS_REQUEST_CODE = 10;

    EditText etSearchQuery;
    Button btnSearch;
    GridView gvImages;

    ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>(56);
    ImageResultArrayAdapter imageAdapter;

    AsyncHttpClient client;

    String additionalParams = "";

    private String imageType = "";

    private String imageColor = "";

    private String imageSize = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views
        etSearchQuery = (EditText) findViewById(R.id.etSearchQuery);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        gvImages = (GridView) findViewById(R.id.gvImages);

        imageAdapter = new ImageResultArrayAdapter(this, imageResults);

        gvImages.setAdapter(imageAdapter);

        gvImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long rowId) {
                Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
                ImageResult item = imageAdapter.getItem(position);
                i.putExtra("result", item);
                startActivity(i);
            }
        });

        gvImages.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadPage(page);
            }
        });

        client = new AsyncHttpClient();

    }

    private void loadPage(int page) {
        String query = etSearchQuery.getText().toString();

        int startIndex = page * 8;

        client.get("https://ajax.googleapis.com/ajax/services/search/images?rsz=8&" +
                        "start=" + startIndex + "&v=1.0&q=" + Uri.encode(query) + additionalParams,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject response) {
                        JSONArray imageJsonResults = null;

                        try {
                            imageJsonResults = response.getJSONObject(
                                    "responseData").getJSONArray("results");

                            imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));

//                            Log.d("DEUBG", imageResults.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void onSearchClick(View view) {
        imageResults.clear();
        imageAdapter.notifyDataSetChanged();

        loadPage(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SearchSettings.class);

            i.putExtra(SearchSettings.IMAGE_SIZE, imageSize);
            i.putExtra(SearchSettings.IMAGE_COLOR, imageColor);
            i.putExtra(SearchSettings.IMAGE_TYPE, imageType);

            startActivityForResult(i, SEARCH_SETTINGS_REQUEST_CODE);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == SEARCH_SETTINGS_REQUEST_CODE) {
            // Extract name value from result extras
            imageSize = data.getExtras().getString(SearchSettings.IMAGE_SIZE);
            imageColor = data.getExtras().getString(SearchSettings.IMAGE_COLOR);
            imageType = data.getExtras().getString(SearchSettings.IMAGE_TYPE);

            final String any = "any";
            additionalParams = "";

            if (!imageSize.equals(any)) {
                additionalParams += "&imgsz=" + imageSize;
            }

            if (!imageColor.equals(any)) {
                additionalParams += "&imgcolor=" + imageColor;
            }

            if (!imageType.equals(any)) {
                additionalParams += "&imgtype=" + imageType;
            }


            onSearchClick(null);
        }
    }

}
