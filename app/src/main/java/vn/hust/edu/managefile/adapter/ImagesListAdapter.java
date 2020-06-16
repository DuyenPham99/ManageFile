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
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.io.File;
import java.util.List;

import vn.hust.edu.managefile.R;
import vn.hust.edu.managefile.model.MediaFileListModel;

public class ImagesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<MediaFileListModel> listmediaFile;
    final int THUMB_SIZE = 64;

    public ImagesListAdapter(List<MediaFileListModel> mediaFileListModels) {
        this.listmediaFile = mediaFileListModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View imageView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_item_view, parent, false);
        return new ImagesViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImagesViewHolder imagesViewHolder = (ImagesViewHolder) holder;

        MediaFileListModel itemImage = listmediaFile.get(position);
        imagesViewHolder.lblFileName.setText(itemImage.getFileName());
        imagesViewHolder.lblFileSize.setText(itemImage.getFileSize());
        imagesViewHolder.lblFileCreated.setText(itemImage.getFileCreatedTime().substring(0,19));
        File imgFile = new File(itemImage.getFilePath());
        if (imgFile.exists()) {
            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile( itemImage.getFilePath()),
                    THUMB_SIZE, THUMB_SIZE);
            imagesViewHolder.imgItemIcon.setImageBitmap(ThumbImage);
        }
    }

    @Override
    public int getItemCount() {
        return listmediaFile.size();
    }

    public  class ImagesViewHolder extends RecyclerView.ViewHolder{
        public TextView lblFileName,lblFileSize,lblFileCreated;
        public ImageView imgItemIcon;
        public ImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            lblFileName = (TextView) itemView.findViewById(R.id.file_name);
            lblFileCreated= (TextView) itemView.findViewById(R.id.file_created);
            imgItemIcon = (ImageView) itemView.findViewById(R.id.icon);
            lblFileSize= (TextView) itemView.findViewById(R.id.file_size);
        }
    }
}
