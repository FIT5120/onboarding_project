<!-- HTML -->
<template>
  <div
    id="app"
    :class="
      typeof weather.temp != 'undefined' && weather.temp > 16 ? 'warm' : ''
    "
  >
    <main>
      <div class="search-box">
        <input
          type="text"
          class="search-bar"
          placeholder="Type city name"
          v-model="query"
          @input="searchLocation"
        />

        
      </div>

      <div class="weather-wrap" v-if="weather.name && weather.country">
        <div class="location-box">
          <div class="location">
            {{ weather.name }}, {{ cur_location.suburb }}, {{ weather.country }}
          </div>
          <div class="date">{{ dateBuilder() }}</div>
        </div>

        <div class="weather-box">
          <div class="temp">{{ Math.round(weather.temp) }}°C</div>
          <div class="weather">{{ weather.name }}</div>
          <div class="uvi">UV index: {{ weather.uvi }} </div>
        </div>
      </div>
    </main>
  </div>
</template>

<!-- SCRIPTS -->
<script>
import axios from 'axios'

export default {
  name: "App",
  mounted() {
    this.fetchAllLocations();
    this.fetchWeather();
  },
  data() {
    //fetching API from openweathermap.org
    return {
      api_key: "b6c75eaf141a32191c638baa7ad4d720",
      url_base: "https://api.openweathermap.org/data/3.0/",
      query: "",
      weather: {
        name:"",
        country:"",
        uvi: 0,
        temp:0
      },
      locations: {},
      cur_location: {
        id: 617,
        suburb:'Clayton',
        lat:-38.000,
        lon:143.000
      },
    };
  },
  methods: {
    //get weather data
    fetchWeather() {
      axios.get(`${this.url_base}onecall?lat=${this.cur_location.lat}&lon=${this.cur_location.lon}&appid=${this.api_key}`)
      .then(res=> {
        let name = res.data.current.weather[0].main;
        let country = res.data.timezone;
        let uvi = res.data.current.uvi;
        let temp = res.data.current.temp
        this.weather = {
          name:name,
          country:country,
          uvi:uvi,
          temp:temp - 273.15
        }
        console.log("weather",this.weather)
      })
      .catch(error => {
        console.error('These was an error', error);
      })
    },
    
    //get location
    fetchAllLocations() {
      axios.get("http://localhost:8080/geo")
      .then(res=> {
        this.locations = res.data;
        console.log("地址数据：", this.locations);
      })
      .catch(error => {
        console.error('These was an error', error);
      })
    },

    selectLocation(location) {
      this.cur_location = location;
      this.query = '';
      this.fetchWeather();
    },

    //search location
    searchLocation() {
      axios.get(`http://localhost:8080/geo/search?suburb=${this.query}`)
      .then(res =>{
        this.cur_location = res.data;
      })
      .catch(error => {
        console.error('These was an error', error);
      })
    },

    //creating dates for the weather
    dateBuilder() {
      let d = new Date();
      let months = [
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December",
      ];
      let days = [
        "Sunday",
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
      ];
      let day = days[d.getDay()];
      let date = d.getDate();
      let month = months[d.getMonth()];
      let year = d.getFullYear();
      return `${day}, ${date} ${month} ${year}`;
    },
  },


};
</script>

<!-- STYLING -->
<style>
@import url("https://fonts.googleapis.com/css2?family=Montserrat&display=swap");

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
body {
  font-family: "Montserrat", sans-serif;
}

#app {
  background-image: url("./assets/cold-bg.jpg");
  background-size: cover;
  background-position: bottom;
  transition: 0.4s;
}

#app.warm {
  background-image: url("./assets/warm-bg.jpg");
}

main {
  min-height: 100vh;
  padding: 25px;

  background-image: linear-gradient(
    to bottom,
    rgba(0, 0, 0, 0.25),
    rgba(0, 0, 0, 0.75)
  );
}

.search-box {
  width: 100%;
  margin-bottom: 30px;
}

.search-box .search-bar {
  display: block;
  width: 100%;
  padding: 15px;

  color: #313131;
  font-size: 20px;
  appearance: none;
  border: none;
  outline: none;
  background: none;

  box-shadow: 0px 0px 16px rgba(0, 0, 0, 0.25);
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 0px 16px 0px 16px;
  transition: 0.4s;
}

.search-box .search-bar:focus {
  box-shadow: 0px 0px 16px rgba(0, 0, 0, 0.5);
  background-color: rgba(255, 255, 255, 0.75);
  border-radius: 16px 0px 16px 0px;
}

.location-box .location {
  color: #fff;
  font-size: 32px;
  font-weight: 500;
  text-align: center;
  /* text-shadow: 1px 3px rgba(0, 0, 0, 0.25); */
}

.location-box .date {
  color: #fff;
  font-size: 18px;
  font-weight: 500;
  text-align: center;
  /* font-style: italic; */
}

.weather-box {
  text-align: center;
}

.weather-box .temp {
  display: inline-block;
  padding: 10px 25px;
  color: aliceblue;
  font-size: 95px;
  font-weight: 500;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 16px;
  margin: 30px 0px;
}

.weather-box .weather {
  color: #fff;
  font-size: 40px;
  font-weight: 500;
}

.weather-box .uvi {
  color: #fff;
  font-size: 40px;
  font-weight: 500;
}
</style>
