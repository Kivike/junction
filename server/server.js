var express = require("express");
var bodyParser = require('body-parser');
var mysql = require('mysql');
var fs = require('fs');

var app = express();
app.use(bodyParser.urlencoded({extended: false }))
app.use(bodyParser.json());

var pool = mysql.createPool({
  connectionLimit: 50,
  host:'localhost',
  user:'junction',
  password:'2015',
  database:'junction'
});

var index = fs.readFileSync('open/index.html');

app.use(express.static('open'));

app.get('/', function (req, res) {
  res.setHeader('Content-Type', 'text/plain');
  res.end(index);
  return;
});

app.post('/newuser', function(req, res){
  pool.getConnection(function(err, connection) {
    if(err){
      res.status(500).send("Cannot currently access database. Try again in couple minutes");
	console.log("Error getting connection from pool " + err);
      return;
    } 

    var sql = "INSERT INTO users (account, location, interests, travelrange) VALUES (" + connection.escape(req.body.account) + "," +
                                                                            connection.escape(req.body.location) + "," +
                                                                            connection.escape(req.body.interests) + "," +  
                                                                            connection.escape(req.body.travelrange) + ")";
    connection.query(sql, function(err, results, fields){
      if(err){
	console.log("error with query " + err);
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
	console.log(err);
      res.status(500).send("Cannot currently access database. Try again in couple minutes");
      return;
    }
    
    connection.query("SELECT * FROM events", function(err, results, fields) {
      if(err){
        res.status(500).send("Invalid database query. Check fields and try again.");
        return;
      }
      
      var sports = results;

      var sql = "SELECT * FROM users WHERE account = " + connection.escape(req.body.account);
    
      connection.query(sql, function(err, results, fields) {
        if(err){
          res.status(500).send("Invalid database query. Check fields and try again.");
          return;
        }
        var userlocation = results[0].location;

        var userCoords = userlocation.location.split(" ");
        var userLatitude = parseFloat(userCoords[0]);
        var userLongitude = parseFloat(userCoords[1]);

        var usertravelrange = parseFloat(results[0].travelrange);
        var possibleSports = [];

        for(var i = 0; i < sports.length; i++){

          var sportsCoords = sports[i].location.split(" ");
          var sportsLatitude = parseFloat(sportsCoords[0]);
          var sportsLongitude = parseFloat(sportsCoords[1]);

          if(coordinatesToDistance(userLatitude, userLongitude, sportsLatitude, sportsLongitude) < travelrange){
            possibleSports.push(results[i]);
          }
        }
        res.status(200).send(possibleSports);
      });
    });
    connection.release();
  });
});

function coordinatesToDistance(lat1, lon1, lat2, lon2){
  var R = 6371000; // metres
  var φ1 = lat1.toRadians();
  var φ2 = lat2.toRadians();
  var Δφ = (lat2-lat1).toRadians();
  var Δλ = (lon2-lon1).toRadians();

  var a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
          Math.cos(φ1) * Math.cos(φ2) *
          Math.sin(Δλ/2) * Math.sin(Δλ/2);
  var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

  var d = R * c;
}

var server = app.listen(8000, function () {

  var host = server.address().address;
  var port = server.address().port;

  console.log('Application listening at http://%s:%s', host, port);

});
