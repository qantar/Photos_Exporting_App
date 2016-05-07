//Modules
var app = require('express').createServer();
var express = require('express');
var fs = require('fs');
var im = require('imagemagick');
 
//Express's powerful parser
app.use(express.bodyParser())
 
//To test on browser root url is a simple html form to upload files
app.get('/', function(req, res){
  res.send(
      '<form action="/upload" method="post" enctype="multipart/form-data">'+
      '<input type="file" name="source">'+
      '<input type="submit" value="Upload">'+
      '</form>'
  );
});
 
//The upload picture request handler
app.post('/upload', function(req, res){
  //This debugging meassage displays all the info that comes with the file
  console.log("Received file:\n" + JSON.stringify(req.files));
 
  //Set the directory names
  var photoDir = __dirname+"/photos/";
  var thumbnailsDir = __dirname+"/photos/thumbnails/";
  var photoName = req.files.source.name;
 
  //We use Node's FileSystem to rename the file, which actually moves it from the /tmp/ folder it goes to on Linux
  fs.rename(
    req.files.source.path,
    photoDir+photoName,
    function(err){
      if(err != null){
        res.send({error:"Server Writting No Good"});
      } else {
        //If upload goes ok we go ahead an create the thumbnail
        im.resize(
          {
            srcData:fs.readFileSync(photoDir+photoName, 'binary'),
            width:256
          }, 
          function(err, stdout, stderr){
            if(err != null){
              res.send({error:"Resizeing No Good"});
            } else {
              fs.writeFileSync(thumbnailsDir+"thumb_"+photoName, stdout, 'binary');
              res.send("Ok");
            }
          }
        );
      }
    }
  );
});
 
app.listen(1337);