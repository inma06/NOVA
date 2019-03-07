package com.teamnova.bongapp.FetchBooks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.teamnova.bongapp.MainActivity;
import com.teamnova.bongapp.R;
import com.teamnova.bongapp.Timer.CustomScanner;

import java.util.ArrayList;
import java.util.List;

public class FetchMain extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<book>> {

    private static final String bookFetchUrl = "https://www.googleapis.com/books/v1/volumes";
//    private static final String bookFetchUrl = "https://dapi.kakao.com/v3/search/book?";

    private RecyclerView recyclerView;
    public FetchBooksAdapter adapter;
    /* Arraylist is static so that it binds with instance of class
    * and we dont have to initialize again in else under onCreate*/
    public static ArrayList<book> bookList = null;
    private static final int BOOKS_LOADER_ID = 1;
    private EditText searchBox;
    private ProgressBar books_progressBar;
    private TextView empty_state;
    private Button barcodeFinder;
    private static String barcodeResult;
    private Spinner spinner;
    public static String selectStr;

    public static Activity fetchMainActivity;


    public static final String TAG = "MyActivity";


    //뒤로가기 눌렀을때 메인액티비티로 이동
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FetchMain.this, MainActivity.class);
        startActivity(intent);
        FetchMain.this.finish();
    }


    //List가 생성될 때 searchBox의 값을 get 하여서 API에 query를 요청한다.
    @Override
    public Loader<List<book>> onCreateLoader(int id, Bundle args) {
        ///BUT How to convert spaces
        String query = searchBox.getText().toString();
        //searchBox가 비어있을때
        if(query.isEmpty() || query.length() == 0){
            searchBox.setError("검색할 책을 입력하세요");
            return new booksLoader(this, null);
        }

        //WITH URI
        Uri baseUri = Uri.parse(bookFetchUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", query);

        //when we click on searchButton keyboard will hide
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        searchBox.setText("");

        //Returning a new Loader Object
        return new booksLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<book>> loader, List<book> list) {
        books_progressBar.setVisibility(View.GONE);
        if(list !=null && !list.isEmpty()){
            prepareBooks(list);
            Log.i(TAG, "onLoadFinished: ");
        }
        else{
            empty_state.setText("찾으시는 책 정보가 없습니다.");
            empty_state.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<book>> loader) {
        Log.i(QueryUtils.TAG, "onLoaderReset: ");
        if(adapter == null){
            return;
        }
        bookList.clear();
        adapter.notifyDataSetChanged();
        Log.i(TAG, "onLoaderReset: " + bookList);
    }

    //바코드 Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            barcodeResult = scanResult.getContents();
            searchBox.setText(barcodeResult);
            if(searchBox.getText().length() > 0) {
                books_progressBar.setVisibility(View.VISIBLE);
                bookList.clear();
                adapter.notifyDataSetChanged();
                getLoaderManager().restartLoader(BOOKS_LOADER_ID, null, this);
                getLoaderManager().initLoader(BOOKS_LOADER_ID, null, this);
                Log.i(TAG, "바코드 검색"  + bookList);
            }
            Log.d(TAG, "onActivityResult:" + barcodeResult);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fetch_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fetchMainActivity = FetchMain.this;
        //스피너 xml 연결
        spinner = (Spinner) findViewById(R.id.spinner);

        barcodeFinder = (Button) findViewById(R.id.barcodeFinder);
        books_progressBar = (ProgressBar) findViewById(R.id.books_progressBar);
        books_progressBar.setIndeterminate(true);
        books_progressBar.setVisibility(View.GONE);

        empty_state = (TextView) findViewById(R.id.empty_state);
        searchBox = (EditText) findViewById(R.id.searchBox);

        //스피너에 들어갈 문자열들
        String[] str = {"제목","저자","출판사"};

        //스피너 어댑터 생성
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String >(this, R.layout.spinner_item, str);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        //스피너 이벤트 리스너
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner.getSelectedItemPosition() > 0) {
                    //선택된 항목
                    Log.d(TAG, "선택된 항목: " + spinner.getSelectedItem().toString());
                    //선택된 항목을 selectStr 변수에 담는다. Google API 리턴값에서 파싱할때 사용할것.
                    selectStr = spinner.getSelectedItem().toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

//        바코드 검색
        barcodeFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //다이얼로그를 띄워서 바코드 스캐너를 실행할 것인지 묻는다.
                AlertDialog.Builder ad = new AlertDialog.Builder(FetchMain.this);
                ad.setTitle("바코드 스캐너");
                ad.setMessage("바코드로 검색 하시겠습니까?");
                //확인버튼
                ad.setPositiveButton("검색", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        IntentIntegrator integrator = new IntentIntegrator(FetchMain.this);
                        integrator.setBeepEnabled(true);
                        integrator.setCaptureActivity(CustomScanner.class);
                        integrator.initiateScan();
                    }
                });
                //취소버튼
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                //다이얼로그 띄우기
                ad.show();
            }
        });

        //Checking the Network State
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null){
            Toast.makeText(this, "인터넷 연결을 확인하세요", Toast.LENGTH_SHORT).show();
            ((Button) findViewById(R.id.searchButton)).setEnabled(false);
        } else if(networkInfo != null) {

        }

        initCollapsingToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        if(savedInstanceState == null || !savedInstanceState.containsKey("booksList")){
            bookList = new ArrayList<>();
            adapter = new FetchBooksAdapter(this, bookList);

            //log Statement
            Log.i(TAG, "onCreate: " + bookList);
        }else {
            bookList.addAll(savedInstanceState.<book>getParcelableArrayList("booksList"));

            //log statement
            Log.i(TAG, "onCreate: under else" + bookList );
            adapter = new FetchBooksAdapter(this, bookList);
            //this will reLoad the adapter
            adapter.notifyDataSetChanged();
        }


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //getLoaderManager().initLoader(BOOKS_LOADER_ID, null, this);


        try {
            Glide.with(this).load(R.drawable.girl).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("booksList", bookList);
        super.onSaveInstanceState(outState);
    }

    public void searchButton(View view){
        //이전에 검색된 리스트를 초기화
        books_progressBar.setVisibility(View.VISIBLE);
        bookList.clear();
        adapter.notifyDataSetChanged();
        getLoaderManager().restartLoader(BOOKS_LOADER_ID, null, this);
        getLoaderManager().initLoader(BOOKS_LOADER_ID, null, this);
        Log.i(TAG, "searchButton: "  + bookList);
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        //ERROR:  This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_SUPPORT_ACTION_BAR and
        // set windowActionBar to false in your theme to use a Toolbar instead.


        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


    //파싱된 값들 List에 추가
    private void prepareBooks(List<book> booksList) {

        bookList.addAll(booksList);
        Log.i(TAG, "prepareBooks: " + bookList);

        //notifiying the recycleradapter that data has been changed
        adapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            // 한줄에 2개의 아이템
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     * dp를 px로 바꾸어 주는 소스
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}