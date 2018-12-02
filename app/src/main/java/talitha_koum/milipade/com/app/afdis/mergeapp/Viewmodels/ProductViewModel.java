package talitha_koum.milipade.com.app.afdis.mergeapp.Viewmodels;

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import talitha_koum.milipade.com.app.afdis.mergeapp.models.Product;
import talitha_koum.milipade.com.app.afdis.mergeapp.models.StockTake;
import talitha_koum.milipade.com.app.afdis.mergeapp.repository.AfdisRepository;


/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */

public class ProductViewModel extends AndroidViewModel {

    private AfdisRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<Product>> mAllProducts;

    public ProductViewModel(Application application) {
        super(application);
        mRepository = new AfdisRepository(application);
        mAllProducts = mRepository.getAllProducts();
    }

    public LiveData<List<Product>> getAllProducts() {
        return mAllProducts;
    }

    public void insert(Product word) {
        mRepository.insert(word);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public LiveData<List<Product>> getProducts(String name) {
        return mRepository.getProduct(name);
    }

    public void insertStock(StockTake stock) {
        mRepository.insertStock(stock);
    }
}