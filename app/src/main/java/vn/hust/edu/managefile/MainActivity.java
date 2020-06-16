package vn.hust.edu.managefile;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import vn.hust.edu.managefile.adapter.InternalStorageListAdapter;
import vn.hust.edu.managefile.model.InternalStorageFilesModel;

public class MainActivity extends AppCompatActivity {
    private String rootPath;
    InternalStorageListAdapter internalStorageListAdapter;
    ArrayList<InternalStorageFilesModel> internalStorageFilesModelArrayList = new ArrayList<>();
    private final HashMap selectedFileHashMap = new HashMap();
    private RecyclerView recyclerView;
    private ArrayList<String> arrayListFilePaths;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        internalStorageListAdapter= new InternalStorageListAdapter(internalStorageFilesModelArrayList);
        recyclerView = findViewById(R.id.recycler_view);
        arrayListFilePaths = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(internalStorageListAdapter);
        arrayListFilePaths.add(rootPath);
        getFilesList(rootPath);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_storage, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_new_folder) {
            createNewFolder();
        } else if (id == R.id.action_new_file) {
            createNewFile();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select action");
        menu.add(0, 0 ,0, "Move");
        menu.add(0, 1, 0, "Rename");
        menu.add(0, 2, 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
    }

    public void createNewFile(){
        final Dialog dialogNewFile = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialogNewFile.setContentView(R.layout.custom_new_file_dialog);
        dialogNewFile.show();
        final EditText txtNewFile = (EditText) dialogNewFile.findViewById(R.id.txt_new_folder);
        Button btnCreate = (Button) dialogNewFile.findViewById(R.id.btn_create);
        Button btnCancel = (Button) dialogNewFile.findViewById(R.id.btn_cancel);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filename = txtNewFile.getText().toString().trim();
                if (filename.length() == 0) {//if file name is empty
                    filename = "NewFile";
                }
                try {
                    File file = new File(rootPath + "/" + filename + ".txt");
                    if (file.exists()) {
                        Toast.makeText(getApplicationContext(), "File already exits", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean isCreated = file.createNewFile();
                        if (isCreated) {
                            InternalStorageFilesModel model = new InternalStorageFilesModel(filename+".txt", file.getPath());
                            internalStorageFilesModelArrayList.add(model);
                            internalStorageListAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "File created.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(),  "File not created..!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialogNewFile.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtNewFile.setText("");
                dialogNewFile.dismiss();
            }
        });
    }

    public void createNewFolder(){
        final Dialog dialogNewFolder = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialogNewFolder.setContentView(R.layout.custom_new_folder_dialog);
        dialogNewFolder.show();
        final EditText txtNewFolder = (EditText) dialogNewFolder.findViewById(R.id.txt_new_folder);
        Button btnCreate = (Button) dialogNewFolder.findViewById(R.id.btn_create);
        Button btnCancel = (Button) dialogNewFolder.findViewById(R.id.btn_cancel);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String folderName = txtNewFolder.getText().toString().trim();
                if (folderName.length() == 0) {//if user not enter text file name
                    folderName = "NewFolder";
                }
                try {
                    File file = new File(rootPath + "/" + folderName);
                    if (file.exists()) {
                        Toast.makeText(getApplicationContext(), "Folder  already exits", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean isFolderCreated = file.mkdir();
                        if (isFolderCreated) {
                            InternalStorageFilesModel model = new InternalStorageFilesModel(folderName, rootPath + "/" + folderName);
                            internalStorageFilesModelArrayList.add(model);
                            internalStorageListAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "Folder created.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Folder not created ..!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialogNewFolder.cancel();
            }

        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtNewFolder.setText("");
                dialogNewFolder.dismiss();
            }
        });

    }

    private void deleteFile(final int selectedFilePosition) {
        final Dialog dialogDeleteFile = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialogDeleteFile.setContentView(R.layout.custom_delete_file_dialog);
        dialogDeleteFile.show();
        Button btnOkay = (Button) dialogDeleteFile.findViewById(R.id.btn_okay);
        Button btnCancel = (Button) dialogDeleteFile.findViewById(R.id.btn_cancel);
        TextView lblDeleteFile = (TextView) dialogDeleteFile.findViewById(R.id.id_lbl_delete_files);
        if (selectedFileHashMap.size() == 1) {
            lblDeleteFile.setText("Are you sure to delete this file?");
        } else {
            lblDeleteFile.setText("Are you sure you want to delete the selected files?");
        }
        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Set set = selectedFileHashMap.keySet();
                    Iterator itr = set.iterator();
                    while (itr.hasNext()) {
                        int i = Integer.parseInt(itr.next().toString());
                        File deleteFile = new File((String) selectedFileHashMap.get(i));//create file for selected file
                        boolean isDeleteFile = deleteFile.delete();//delete the file from memory
                        if (isDeleteFile) {
                            selectedFileHashMap.remove(i);
                            InternalStorageFilesModel model = internalStorageFilesModelArrayList.get(i);
                            internalStorageFilesModelArrayList.remove(model);//remove file from listview
                            internalStorageListAdapter.notifyDataSetChanged();//refresh the adapter
                            selectedFileHashMap.remove(selectedFilePosition);
                        }
                    }
                    dialogDeleteFile.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDeleteFile.dismiss();
            }
        });
    }

    private void renameFile(final Dialog menuDialog, String fileName, final String filePath, final int selectedFilePosition) {
        final Dialog dialogRenameFile = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialogRenameFile.setContentView(R.layout.custom_rename_file_dialog);
        dialogRenameFile.show();
        final EditText txtRenameFile = (EditText) dialogRenameFile.findViewById(R.id.txt_file_name);
        Button btnRename = (Button) dialogRenameFile.findViewById(R.id.btn_rename);
        Button btnCancel = (Button) dialogRenameFile.findViewById(R.id.btn_cancel);
        txtRenameFile.setText(fileName);
        btnRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtRenameFile.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter file name", Toast.LENGTH_SHORT).show();
                } else {
                    File renamedFile = new File(filePath.substring(0, filePath.lastIndexOf('/') + 1) + txtRenameFile.getText().toString());
                    if (renamedFile.exists()) {
                        Toast.makeText(getApplicationContext(), "File already exits,choose another name", Toast.LENGTH_SHORT).show();
                    } else {
                        final File oldFile = new File(filePath);//create file with old name
                        boolean isRenamed = oldFile.renameTo(renamedFile);
                        if (isRenamed) {
                            InternalStorageFilesModel model = internalStorageFilesModelArrayList.get(selectedFilePosition);
                            model.setFileName(txtRenameFile.getText().toString());
                            model.setFilePath(renamedFile.getPath());
                          /*  if (renamedFile.isDirectory()) {
                                model.setIsDir(true);
                            } else {
                                model.setIsDir(false);
                            } */
                            internalStorageFilesModelArrayList.remove(selectedFilePosition);
                            internalStorageFilesModelArrayList.add(selectedFilePosition, model);
                            internalStorageListAdapter.notifyDataSetChanged();
                            dialogRenameFile.dismiss();
                            menuDialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "Fail to renamed ..!" , Toast.LENGTH_SHORT).show();
                            dialogRenameFile.dismiss();
                            menuDialog.dismiss();
                        }
                    }
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtRenameFile.setText("");
                dialogRenameFile.dismiss();
            }
        });
    }

    private void getFilesList(String filePath) {
        rootPath = filePath;
       // lblFilePath.setText(filePath);
        File f = new File(filePath);
        File[] files = f.listFiles();
        if (files != null) {
            for (File file : files) {
                InternalStorageFilesModel model = new InternalStorageFilesModel();
                model.setFileName(file.getName());
                model.setFilePath(file.getPath());
                if (file.getName().indexOf('.') != 0) {
                    internalStorageFilesModelArrayList.add(model);
                }
            }
        }
    }

    private void showMenu(final int selectedFilePosition) {
        final Dialog menuDialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        menuDialog.setContentView(R.layout.custom_menu_dialog);
        TextView lblRenameFile = (TextView) menuDialog.findViewById(R.id.id_rename);
        TextView lblFileDetails = (TextView) menuDialog.findViewById(R.id.id_file_details);
        TextView lblFileMove = (TextView) menuDialog.findViewById(R.id.id_move);
        TextView lblFileDelete = (TextView) menuDialog.findViewById(R.id.id_file_delete);
        if (selectedFileHashMap.size() == 1) {
            lblRenameFile.setClickable(true);
            lblRenameFile.setFocusable(true);
            lblFileMove.setClickable(true);
            lblFileMove.setFocusable(true);
            lblFileDetails.setFocusable(true);
            lblFileDetails.setClickable(true);
            lblRenameFile.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_text_selected));
            lblFileMove.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_text_selected));
            lblFileDetails.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_text_selected));
        } else {
            lblRenameFile.setClickable(false);
            lblRenameFile.setFocusable(false);
            lblFileMove.setClickable(false);
            lblFileMove.setFocusable(false);
            lblFileDetails.setFocusable(false);
            lblFileDetails.setClickable(false);
            lblFileDetails.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_text_unselected));
            lblRenameFile.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_text_unselected));
            lblFileMove.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_text_unselected));
        }

        lblFileMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuDialog.dismiss();
                //fileMoveLayout.setVisibility(View.VISIBLE);
                for (int i = 0; i < internalStorageFilesModelArrayList.size(); i++) {
                    InternalStorageFilesModel internalStorageFilesModel = internalStorageFilesModelArrayList.get(i);
                    //internalStorageFilesModel.setCheckboxVisible(false);
                }
                internalStorageListAdapter.notifyDataSetChanged();
                //isCheckboxVisible = false;
            }
        });
        lblRenameFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InternalStorageFilesModel internalStorageFilesModel = internalStorageFilesModelArrayList.get(selectedFilePosition);
                renameFile(menuDialog, internalStorageFilesModel.getFileName(), internalStorageFilesModel.getFilePath(), selectedFilePosition);
            }
        });
        lblFileDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuDialog.dismiss();
            }
        });
        menuDialog.show();
    }

}
