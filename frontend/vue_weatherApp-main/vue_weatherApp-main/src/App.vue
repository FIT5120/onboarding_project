<!-- HTML -->
<template>
  <div
    id="app"
    :class="
      typeof weather.temp != 'undefined' && weather.temp > 20 ? 'warm' : ''
    "
  >
    <main>
      <div class="search-box">
        <el-autocomplete
          type="text"
          class="search-bar"
          :fetch-suggestions="querySearch"
          clearable
          placeholder="Type subrub name"
          v-model="query"
          @select="handleSelect"
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
          <div class="weather">{{ weather.name }} </div>
          <div class="icon"> <img :src="iconData"> </div>
          <div class="uvi">UV index: {{ weather.uvi }}</div>
          <div class="uvi">
            <p :style="{ color: uvLevelColor }">UV Level: {{ uvLevelMessage }}</p>           
          </div>
          <div class="uvi">
            <div class=uvlevel-msg>
              <p style="width: 700px; text-align: center;">Recommandation: {{ uvMessage }}</p>
            </div>
            
          </div>
          <el-button type="primary" style="margin-left: 16px" @click="drawer = true">
            Sunscreen Calculator
          </el-button>

          <el-drawer v-model="drawer" title="I am the title" :with-header="false">
            <h2 style="margin-bottom:10px">Sunscreen Calculator</h2>
            <div style="margin-bottom: 20px;">
              <img style="max-width: 90%; height: auto;" src="./assets/sunscreen.jpg">  
            </div>

            Height: <input type="number" v-model="height" style="margin-bottom:10px; font-size: large;"> cm <br>
            Weight: <input type="number" v-model="weight" style="margin-bottom:10px; font-size: large;"> kg <br>
            <el-button type="primary" @click="calculateSunscreenAmount">Calculate</el-button>
            <div style="margin-top:10px ;" v-if="sunscreenAmount !== null">
              Sunscreen Amount: {{ Math.round(sunscreenAmount) }} ml
            </div>
            
          </el-drawer>
        </div>
        
      </div>
    </main>
  </div>
</template>

<!-- SCRIPTS -->
<script>
import axios from 'axios'
import jsonData from '@/assets/data/locationData.json'

export default {
  name: "App",
  mounted() {
    this.fetchAllLocations();
    this.fetchWeather(this.cur_location.lat, this.cur_location.lon);
  },
  data() {
    //fetching API from openweathermap.org
    return {
      drawer:false,
      api_key: "15cc9b4fa29b5c23c78cbde85c4ec55c",
      url_base: "https://api.openweathermap.org/data/3.0/",
      query: "",
      weather: {
        name:"",
        country:"",
        uvi: 0,
        temp:0,
        icon:""
      },
      iconData: null,
      locations: [],
      filteredSuburbs:[],
      suburbList: [],
      cur_location: {
        id: 617,
        suburb:'Clayton',
        lat:-38.000,
        lon:143.000
      },
      height: null,
      weight: null,
      sunscreenAmount: null,
    };
  },

  computed: {
    uvLevelMessage() {
      if (this.weather.uvi >= 0 && this.weather.uvi < 3) {
        return "Low";
      } else if (this.weather.uvi >= 3 && this.weather.uvi < 6) {
        return "Moderate";
      } else if (this.weather.uvi >= 6 && this.weather.uvi < 8) {
        return "High";
      } else if (this.weather.uvi >= 8 && this.weather.uvi < 11) {
        return "Very High";
      } else if (this.weather.uvi >= 11) {
        return "Extreme";
      }
      return "Invalid UV Rating"; // Default message
    },

    uvLevelColor() {
    if (this.weather.uvi >= 0 && this.weather.uvi < 3) {
      return 'lightseagreen';
    } else if (this.weather.uvi >= 3 && this.weather.uvi < 6) {
      return 'lightgreen';
    } else if (this.weather.uvi >= 6 && this.weather.uvi < 8) {
      return 'yellow';
    } else if (this.weather.uvi >= 8 && this.weather.uvi < 11) {
      return 'orange';
    } else if (this.weather.uvi >= 11) {
      return 'red';
    }
    return 'black'; // Default color
    },

    uvMessage() {
      if(this.weather.uvi == 0) {
        return "You don't need any protection.";
      }
      else if (this.weather.uvi >= 0 && this.weather.uvi < 3) {
        return "Wear sunglasses, sunscreen, a hat if you plan on staying out for a prolonged period.";
      } else if (this.weather.uvi >= 3 && this.weather.uvi < 6) {
        return "Wear sunglasses, sunscreen, a hat, and protective clothing if you plan on staying out for a prolonged period.";
      } else if (this.weather.uvi >= 6 && this.weather.uvi < 8) {
        return "Wear sunglasses, sunscreen, a hat, protective clothing, and try to reduce time in the sun if you plan on staying out for a prolonged period.";
      } else if (this.weather.uvi >= 8) {
        return "Try to stay indoors.";
      }
      return "Check UV rating."; // Default message if UV rating is out of expected range
    },

  },

  methods: {
    //get weather data
    fetchWeather(lat, lon) {
      axios.get(`${this.url_base}onecall?lat=${lat}&lon=${lon}&appid=${this.api_key}`)
      .then(res=> {
        let name = res.data.current.weather[0].main;
        let country = res.data.timezone;
        let uvi = res.data.current.uvi;
        let temp = res.data.current.temp
        let icon = res.data.current.weather[0].icon
        this.weather = {
          name:name,
          country:country,
          uvi:uvi,
          temp:temp - 273.15,
          icon:icon
        }
        this.iconData = `https://openweathermap.org/img/wn/${this.weather.icon}@2x.png`
      })
      .catch(error => {
        console.error('These was an error', error);
      })
    },
    
    //get location
    fetchAllLocations() {
      this.locations = jsonData;
      for(let i in this.locations) {
        this.suburbList.push({value:this.locations[i].suburb})
      }
    },

    //search location
    searchLocation(suburb) {
      for(let i in this.locations) 
      {
        if(suburb == this.locations[i].suburb)
        {
          this.cur_location = this.locations[i];
        }
      }
      console.log("cur_location:",this.cur_location)
      this.fetchWeather(this.cur_location.lat, this.cur_location.lon);
      console.log(this.weather)
    },
   
    querySearch (queryString, cb) {
      const suburbs = this.suburbList;
      const result = queryString
      ? suburbs?.filter(
        (suburb)=> {
          return (suburb.value.toLowerCase().indexOf(queryString.toLowerCase()) == 0)
        }
      )
      : suburbs
      cb(result)
    },
    
    handleSelect(item) {
      this.searchLocation(item.value);
    },

    calculateSunscreenAmount() {
      if (!this.height || !this.weight) {
        alert('Please enter both height and weight.');
        return;
      }
      this.sunscreenAmount = (0.007184 * Math.pow(this.height, 0.725) * Math.pow(this.weight, 0.425)) * 30;
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
  margin-top: 20px;
  margin-bottom: 20px;
}

.weather-box .uvi .uvlevel-msg {
  display: flex; 
  justify-content: center;
}

.weather-box .uvi .uvlevel-msg p{
  width: 700px; 
  text-align: center; 
  font-size: 20px;
}
</style>
