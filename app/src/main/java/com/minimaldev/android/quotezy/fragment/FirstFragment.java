package com.minimaldev.android.quotezy.fragment;

import static com.minimaldev.android.quotezy.constants.Constants.AUTHOR;
import static com.minimaldev.android.quotezy.constants.Constants.BY;
import static com.minimaldev.android.quotezy.constants.Constants.CONTENT;
import static com.minimaldev.android.quotezy.constants.Constants.EMPTY;
import static com.minimaldev.android.quotezy.constants.Constants.FAMOUS_QUOTES;
import static com.minimaldev.android.quotezy.constants.Constants.POPULAR;
import static com.minimaldev.android.quotezy.constants.Constants.TAGS;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.minimaldev.android.quotezy.R;
import com.minimaldev.android.quotezy.databinding.FragmentFirstBinding;
import com.minimaldev.android.quotezy.helper.ISetResponse;
import com.minimaldev.android.quotezy.model.Quote;
import com.minimaldev.android.quotezy.service.QuoteServiceImpl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FirstFragment extends Fragment implements ISetResponse {

    private FragmentFirstBinding binding;
    private static final String TAG = FirstFragment.class.getName();
    private TextView quoteText;
    private TextView authorText;
    private MutableLiveData<Quote> response;
    private QuoteServiceImpl service;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        try{
            super.onViewCreated(view, savedInstanceState);
            quoteText = view.findViewById(R.id.quote_text);
            authorText = view.findViewById(R.id.author_text);
            service = new QuoteServiceImpl(getActivity().getApplicationContext(), this);
            observeResponse();
            prepareChipsListenerRequest(view);
        } catch (Exception e){
            Log.e(TAG, "An exception occurred at onViewCreated() : ", e);
        }
    }

    private void prepareChipsListenerRequest(@NonNull View view) {
        ChipGroup chipGroup = view.findViewById(R.id.chip_group);
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = view.findViewById(checkedId);
            //Call to service API
            if(!Objects.isNull(chip) && chip.isChecked()){
                getQuoteByTags(chip.getText().toString());
            }
        });

    }

    private void observeResponse() {
        response = new MutableLiveData<>();
        response.setValue(new Quote());
        response.observeForever(quote -> {
            quoteText.setText(quote.getText());
            authorText.setText(quote.getAuthor() == null ? EMPTY : BY + quote.getAuthor());
        });
    }

    private void getQuoteTags() {
        service.getQuoteTags();
    }

    public void getQuoteByTags(String tagValue){
        if(POPULAR.equalsIgnoreCase(tagValue)){
            tagValue = FAMOUS_QUOTES;
        }
        service.getQuotesByTag(tagValue);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void setQuoteTagResponse(String response) {
        Quote quote = parseResponse(response);
        this.response.postValue(quote);
    }

    private Quote parseResponse(String response) {
        Quote quote = new Quote();
        try{
            JSONObject parentObject = new JSONObject(response);
            quote.setText(parentObject.getString(CONTENT));
            quote.setAuthor(parentObject.getString(AUTHOR));
            JSONArray tags = parentObject.getJSONArray(TAGS);
            List<String> tagList = new ArrayList<>();
            for(int index = 0; index < tags.length(); index++){
                tagList.add(tags.getString(index));
            }
            quote.setTag(tagList);
            Log.d(TAG, "Final quote : " + quote);
        } catch (Exception e){
            Log.e(TAG, "An exception occurred while parsing json : ", e);
        }
        return quote;
    }
}