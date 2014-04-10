package io.gautam.codepath.imagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class SearchSettings extends Activity {

    public final static String IMAGE_SIZE = "image_size";
    public final static String IMAGE_COLOR = "image_color";
    public final static String IMAGE_TYPE = "image_type";

    Spinner spImageSize;
    Spinner spImageColor;
    Spinner spImageType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_settings);

        spImageSize = (Spinner) findViewById(R.id.spImageSize);
        spImageColor = (Spinner) findViewById(R.id.spImageColor);
        spImageType = (Spinner) findViewById(R.id.spImageType);

        ArrayAdapter<CharSequence> imageSizesAdapter = ArrayAdapter.createFromResource(this, R.array.image_sizes_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> imageColorsAdapter = ArrayAdapter.createFromResource(this, R.array.image_colors_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> imageTypesAdapter = ArrayAdapter.createFromResource(this, R.array.image_types_array, android.R.layout.simple_spinner_item);

        imageSizesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        imageColorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        imageTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spImageSize.setAdapter(imageSizesAdapter);
        spImageColor.setAdapter(imageColorsAdapter);
        spImageType.setAdapter(imageTypesAdapter);

        String imageSize = getIntent().getStringExtra(IMAGE_SIZE);
        String imageColor = getIntent().getStringExtra(IMAGE_COLOR);
        String imageType = getIntent().getStringExtra(IMAGE_TYPE);

        if (!imageColor.equals(""))
            spImageColor.setSelection(imageColorsAdapter.getPosition(imageColor));

        if (!imageSize.equals(""))
            spImageSize.setSelection(imageSizesAdapter.getPosition(imageSize));

        if (!imageType.equals(""))
            spImageType.setSelection(imageTypesAdapter.getPosition(imageType));

    }


    public void onCancelClick(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onSaveClick(View view) {
        Intent data = new Intent();

        data.putExtra(IMAGE_SIZE, spImageSize.getSelectedItem().toString());
        data.putExtra(IMAGE_COLOR, spImageColor.getSelectedItem().toString());
        data.putExtra(IMAGE_TYPE, spImageType.getSelectedItem().toString());

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
