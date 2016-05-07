
//PreExcute
protected void onPreExecute() {
   //Set timeout parameters
   int timeout = 10000;
   HttpParams httpParameters = new BasicHttpParams();
   HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
   HttpConnectionParams.setSoTimeout(httpParameters, timeout);
 
   //We'll use the DefaultHttpClient
   client = new DefaultHttpClient(httpParameters);
 
   //Show the Progress Dialog
   pd = new ProgressDialog(context);
   pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
   pd.setMessage("Uploading Picture...");
   pd.setCancelable(false);
   pd.show();
}

//Background
@Override
protected Void doInBackground(Void... params) {
  try {
     File file = new File(imgPath);
 
     //Create the POST object
     HttpPost post = new HttpPost(url);
 
     //Create the multipart entity object and add a progress listener
     //this is a our extended class so we can know the bytes that have been transfered
     MultipartEntity entity = new MyMultipartEntity(new ProgressListener()
     {
        @Override
        public void transferred(long num)
        {
           //Call the onProgressUpdate method with the percent completed
           publishProgress((int) ((num / (float) totalSize) * 100));
           Log.d("DEBUG", num + " - " + totalSize);
        }
     });
     //Add the file to the content's body
     ContentBody cbFile = new FileBody( file, "image/jpeg" );
     entity.addPart("source", cbFile);
 
     //After adding everything we get the content's lenght
     totalSize = entity.getContentLength();
 
     //We add the entity to the post request
     post.setEntity(entity);
 
     //Execute post request
      HttpResponse response = client.execute( post );
     int statusCode = response.getStatusLine().getStatusCode();
 
      if(statusCode == HttpStatus.SC_OK){
         //If everything goes ok, we can get the response
        String fullRes = EntityUtils.toString(response.getEntity());
        Log.d("DEBUG", fullRes);
 
     } else {
        Log.d("DEBUG", "HTTP Fail, Response Code: " + statusCode);
     }
 
  } catch (ClientProtocolException e) {
     // Any error related to the Http Protocol (e.g. malformed url)
     e.printStackTrace();
  } catch (IOException e) {
     // Any IO error (e.g. File not found)
     e.printStackTrace();
  }
 
  return null;
}
//Progress
@Override
protected void onProgressUpdate(Integer... progress) {
//Set the pertange done in the progress dialog
pd.setProgress((int) (progress[0]));
}


//postExcute
@Override
protected void onPostExecute(Void result) {
  //Dismiss progress dialog
  pd.dismiss();
}