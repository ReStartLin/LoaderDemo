package restart.com.loderdemo;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText mSearch;
    private ListView mLinkman;
    private LoaderManager loaderManager;
    private SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearch = findViewById(R.id.id_edt_search);
        mLinkman = findViewById(R.id.id_lv_linkman);
        simpleCursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                new int[]{android.R.id.text1},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mLinkman.setAdapter(simpleCursorAdapter);

        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Bundle bundle = null;
                if (!TextUtils.isEmpty(s.toString())){
                    bundle = new Bundle();
                    bundle.putString("filter",s.toString());
                }
                loaderManager.restartLoader(0, bundle, MainActivity.this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(0, null, this);
    }

    /**
     * Loader初始化方法
     * @param id
     * @param args
     * @return
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        if (args != null) {
            String filter = args.getString("filter");
                uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI,
                        Uri.encode(filter));

        }
        CursorLoader cursorLoader = new CursorLoader(this,
                uri, null, null, null, null);
        return cursorLoader;
    }

    /**
     * loader结束后执行  运行在主线程中
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Cursor cursor = simpleCursorAdapter.swapCursor(data);
        if (cursor != null) {
            cursor.close();
        }

    }
    //loader 重置
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Cursor cursor = simpleCursorAdapter.swapCursor(null);
        if (cursor != null) {
            cursor.close();
        }
    }
}
