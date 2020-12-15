//package com.example.weatherapp.fragments;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.loader.app.LoaderManager;
//import androidx.loader.content.Loader;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//import android.widget.CheckBox;
//
//import com.example.weatherapp.R;
//import com.example.weatherapp.WeatherDescription;
//import com.example.weatherapp.helper.AllCity;
//import com.example.weatherapp.helper.JsonFileReaderTask;
//import com.example.weatherapp.helper.Keys;
//import com.google.android.material.snackbar.Snackbar;
//import com.google.android.material.textfield.TextInputEditText;
//
//import java.util.ArrayList;
//import java.util.concurrent.ExecutionException;
//
//
//public class MainFunctionsFragment extends Fragment implements LoaderManager.LoaderCallbacks {
//    private final static String TAG = MainFunctionsFragment.class.getSimpleName();
//    private static final int REQUEST_CODE = 1;
//    private AutoCompleteTextView autoCompleteTextView;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View layout = inflater.inflate(R.layout.fragment_main_functions, container, false);
//        autoCompleteTextView = layout.findViewById(R.id.textInput_enter_city);
//        if (getActivity()!=null) {
//
//
//
//        }
//        //JsonFileReaderTask task = new JsonFileReaderTask(getActivity());
//       // ArrayList <String> cities = JsonReading.read(getActivity(), "city.list.json");
//       // ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, cities);
//        //autoCompleteTextView.setDropDownBackgroundResource((R.color.green));
//      //  autoCompleteTextView.setAdapter(adapter);
//        Button button_show = layout.findViewById(R.id.button_show);
//        button_show.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), WeatherDescription.class);
//                String value = autoCompleteTextView.getText().toString().trim();
//                if (!value.isEmpty()) {
//                    Log.d(TAG, "передача бандла " + value);
//                    intent.putExtra(Keys.CITY, value);
//                    startActivityForResult(intent, REQUEST_CODE);
//                } else {
//                    Snackbar.make(v, R.string.enter_city, Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                }
//            }
//        });
//        return layout;
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        getActivity();
//        Log.v(TAG, " выполние onActivityResult");
//        if (requestCode != REQUEST_CODE) {
//            super.onActivityResult(requestCode, resultCode, data);
//            return;
//        }
//        if (resultCode == Activity.RESULT_OK) {
//            Log.v(TAG, " получено значение из другой активити " + data.getParcelableExtra(Keys.FAVOURITES));
//        }
//    }
//
//    @NonNull
//    @Override
//    public Loader onCreateLoader(int id, @Nullable Bundle args) {
//        return null;
//    }
//
//    @Override
//    public void onLoadFinished(@NonNull Loader loader, Object data) {
//
//    }
//
//    @Override
//    public void onLoaderReset(@NonNull Loader loader) {
//
//    }
//}
