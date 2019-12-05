package net.learn2develop.myapplication111.DataBase;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import net.learn2develop.myapplication111.AlarmProfile.AlarmProfileActivity;
import net.learn2develop.myapplication111.R;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder> {
    private Context mContext;
    private Cursor mCursor;
   // Context context;

    public GroceryAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }




    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        public TextView profileText;
        public TextView nameText;
        public TextView countText;
        public ImageButton read_datebase;
        //private SQLiteDatabase mDatabase;




        public GroceryViewHolder(View itemView) {
            super(itemView);

            read_datebase =  itemView.findViewById(R.id.read_database);
            profileText = itemView.findViewById(R.id.textview_profile_item);
            nameText = itemView.findViewById(R.id.textview_name_item);
            countText = itemView.findViewById(R.id.textview_amount_item);
        }

      /*  @Override
        public void onClick(View v) {

            DBHelper dbHelper = new DBHelper(context);
            mDatabase = dbHelper.getWritableDatabase();

            String name = nameText.getText().toString();
            String amount = countText.getText().toString();
            String profile = profileText.getText().toString();

            switch (v.getId()) {
                case R.id.read_database:
                    // делаем запрос всех данных из таблицы mytable, получаем Cursor
                    Cursor c = mDatabase.query("groceryList", null, null, null, null, null, null);

                    // ставим позицию курсора на первую строку выборки
                    // если в выборке нет строк, вернется false
                    if (c.moveToFirst()) {

                        // определяем номера столбцов по имени в выборке
                        int nameColIndex = c.getColumnIndex("name");
                        int amountColIndex = c.getColumnIndex("amount");
                        int profileColIndex = c.getColumnIndex("profile");

                        do {

                        } while (c.moveToNext());
                    } else

                    c.close();
                    break;
            }
        }*/
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.grocery_item, parent, false);
        return new GroceryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroceryViewHolder holder, final int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        final String profile = mCursor.getString(mCursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_PROFILE));
        final String name = mCursor.getString(mCursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_TIME));
        final String amount = mCursor.getString(mCursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_DATE));

        holder.profileText.setText(profile);
        holder.nameText.setText(name);
        holder.countText.setText(amount);
        holder.read_datebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProf = new Intent(mContext, AlarmProfileActivity.class);
                intentProf.putExtra("nameText", name);
                intentProf.putExtra("countText", amount);
                intentProf.putExtra("profileText", profile);
                mContext.startActivity(intentProf);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}


