package vn.hust.edu.managefile.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

import vn.hust.edu.managefile.R;
import vn.hust.edu.managefile.model.InternalStorageFilesModel;

public class InternalStorageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<InternalStorageFilesModel> listInternalStorage;

    public InternalStorageListAdapter(List<InternalStorageFilesModel> listInternalStorage) {
        this.listInternalStorage = listInternalStorage;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_list_item_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder =(MyViewHolder) holder;

        InternalStorageFilesModel item = listInternalStorage.get(position);

        viewHolder.lblFileName.setText(item.getFileName());
        String fileExtension =item.getFileName().substring(item.getFileName().lastIndexOf(".")+1);
        File file = new File(item.getFilePath());
        if(file.isDirectory()){
            viewHolder.imgItemIcon.setImageResource(R.drawable.ic_folder);
        } else if(fileExtension.equals("txt")){
            viewHolder.imgItemIcon.setImageResource(R.drawable.ic_text_file);
        } else if(fileExtension.equals("png") || fileExtension.equals("jpeg") || fileExtension.equals("jpg")){
            File imgFile = new File(item.getFilePath());
            if (imgFile.exists()) {
                int THUMB_SIZE = 64;
                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(item.getFilePath()),
                        THUMB_SIZE, THUMB_SIZE);
                viewHolder.imgItemIcon.setImageBitmap(ThumbImage);
            }
        } else {
            viewHolder.imgItemIcon.setImageResource(R.drawable.ic_un_supported_file);
        }
    }

    @Override
    public int getItemCount() {
        return listInternalStorage.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder{
        public TextView lblFileName;
        public ImageView imgItemIcon;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            lblFileName = (TextView) itemView.findViewById(R.id.file_folder_name);
            imgItemIcon = (ImageView) itemView.findViewById(R.id.iconImage);
        }
    }
}

