package jagerfield.mobilecontactslibrary;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import jagerfield.mobilecontactslibrary.Contact.Contact;
import jagerfield.mobilecontactslibrary.Utilities.Utilities;


public class ImportContactsAsync extends AsyncTask<Void, Void, Void>
{
    private Activity activity;
    private ICallback client;

    @Expose
    private ArrayList<Contact> contacts;

    private  ArrayList<Contact> suggestedContact;
    private  ArrayList<Contact> nPictureContact;
    private  ArrayList<Contact> contactList;


    public ImportContactsAsync(Activity activity, ICallback client)
    {
        this.activity = activity;
        this.client = client;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        contactList=new ArrayList<>();
        suggestedContact=new ArrayList<>();
        nPictureContact=new ArrayList<>();
    }
    @Override
    protected void onPostExecute(Void aVoid)
    {
        client.mobileContacts(contactList);
    }

    @Override
    protected Void doInBackground(Void... params)
    {
        try
        {
            ImportContacts importContacts = new ImportContacts(activity);
            contacts = importContacts.getContacts();
            for(Contact c:contacts){
                if(c.getPhotoUri()!=null&&!c.getPhotoUri().isEmpty()){
                    if(suggestedContact.size()==0) {
                        c.setType("Suggested Contact");
                    }else{
                        c.setType(null);
                    }
                    suggestedContact.add(c);
                }else{
                    if(nPictureContact.size()==0) {
                        c.setType("Contact");
                    }else{
                        c.setType(null);
                    }
                    //c.setType("Contact");
                    nPictureContact.add(c);
                }
            }
            contactList.addAll(suggestedContact);
            contactList.addAll(nPictureContact);

            //suggested_Contact = importContacts.getContacts();
            String str = "";
        }
        catch(Exception e)
        {
            Log.e(Utilities.TAG_LIB, e.getMessage());
        }

        return null;
    }


    public interface ICallback
    {
        void mobileContacts(ArrayList<Contact> suggestedContact);

    }

}

