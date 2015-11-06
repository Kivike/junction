var express = require("express");
var bodyParser = require('body-parser');
var mysql = require('mysql');

var pool = mysql.createPool({
  connectionLimit: 50,
  host:'localhost',
  user:'junction',
  password:'2015',
  database:'junction',
});

var index = fs.readFileSync('website/index.html');

app.use(express.static('website'));

app.get('/', function (req, res) {
  res.setHeader('Content-Type', 'text/plain');
  res.end(index);
  return;
});

app.post('/newuser', function(req, res){
  pool.getConnection(function(err, connection) {
    if(err){
      res.status(500).send("Cannot currently access database. Try again in couple minutes");
      return;
    } 

    var sql = "INSERT INTO users (account, location, interests, travelrange) VALUES (" + connection.escape(req.body.account) + "," +
                                                                            connection.escape(req.body.location) + "," +
                                                                            connection.escape(req.body.interests) + "," +  
                                                                            connection.escape(req.body.travelrange) + ")";
    connection.query(sql, function(err, results, fields){
      if(err){
        res.status(500).send("Invalid database query. Check fields and try again.");
        return;
      }
      res.status(200).send("Account created succesfully!");
    });
    connection.release();
  });
});

app.post('/changelocation', function(req, res){
  pool.getConnection(function(err, connection){
    if(err){
      res.status(500).send("Cannot currently access database. Try again in couple minutes");
      return;
    }

    var sql = "UPDATE users SET location=" + connection.escape(req.body.location) + " WHERE account=" + connection.escape(req.body.account);
    connection.query(sql, function(err, results, fields){
      if(err){
        res.status(500).send("Invalid database query. Check fields and try again.");
        return;
      }
      res.status(200).send("Location changed succesfully");
    });
    connection.release();
  });
});
  
app.post('/changeinterests', function(req, res){
  pool.getConnection(function(err, connection){
    if(err){
      res.status(500).send("Cannot currently access database. Try again in couple minutes");
      return;
    }
    
    var sql = "UPDATE users SET interests=" + connection.escape(req.body.interests) + " WHERE account =" + connection.escape(req.body.account);
    connection.query(sql, function(err, results, fields) {
      if(err){
        res.status(500).send("Invalid database query. Check fields and try again.");
        return;
      }
      res.status(200).send("Interests changed succesfully");
    });
    connection.release();
  });
});

app.post('/changetravelrange', function(req, res){
  pool.getConnection(function(err, connection){
    if(err){
      res.status(500).send("Cannot currently access database. Try again in couple minutes");
      return;
    }

    var sql = "UPDATE users SET travelrange=" + connection.escape(req.body.travelrange) + " WHERE account=" + connection.escape(req.body.account);
    connection.query(sql, function(err, results, fields){
      if(err){
        res.status(500).send("Invalid database query. Check fields and try again.");
        return;
      }
      res.status(200).send("Range changed succesfully");
    });
    connection.release();
  });
});

app.post('/createEvent', function(req, res){
  pool.getConnection(function(err, connection) {
    if(err){
      res.status(500).send("Cannot currently access database. Try again in couple minutes");
      return;
    } 

    var sql = "INSERT INTO events (location, time, type) VALUES (" + connection.escape(req.body.location) + "," +
                                                                    connection.escape(req.body.time) + "," + 
                                                                    connection.escape(req.body.type) + ")";                                                                           connection.escape(req.body.interests) + ")";
    connection.query(sql, function(err, results, fields){
      if(err){
        res.status(500).send("Invalid database query. Check fields and try again.");
        return;
      }
      res.status(200).send("Event created succesfully!");
    });
    connection.release();
  });
});

app.get('/events', function(req, res){
  pool.getConnection(function(err, connection){
    if(err){
      res.status(500).send("Cannot currently access database. Try again in couple minutes");
      return;
    }
    
    connection.query("SELECT * FROM events", function(err, results, fields) {
      if(err){
        res.status(500).send("Invalid database query. Check fields and try again.");
        return;
      }
      
      var sports = results;

      }

      var sql = "SELECT * FROM users WHERE account = " + connection.escape(req.body.account);
    
      connection.query(sql, function(err, results, fields) {
        if(err){
          res.status(500).send("Invalid database query. Check fields and try again.");
          return;
        }
        var userlocation = results[0].location;
        var possibleSports = [];

        for(var i = 0; i < results.length; i++){
          if(results[i].location * results[i].location + userlocation * userlocation < range * range){
            possibleSports.push(results[i]);
          }
        }
        res.status(200).send(possibleSports);
      });
    });
    connection.release();
  });
});

var server = app.listen(8000, function () {

  var host = server.address().address;
  var port = server.address().port;

  console.log('Application listening at http://%s:%s', host, port);

});