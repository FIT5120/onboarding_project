<!--SampleNavbar.vue-->
<template>
  <nav
    class="navbar navbar-expand-lg navbar-absolute"
    :class="{ 'bg-white': showMenu, 'navbar-transparent': !showMenu }"
  >
    <div class="container-fluid">

      <button
        class="navbar-toggler"
        type="button"
        @click="toggleMenu"
        data-toggle="collapse"
        data-target="#navigation"
        aria-controls="navigation-index"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-bar navbar-kebab"></span>
        <span class="navbar-toggler-bar navbar-kebab"></span>
        <span class="navbar-toggler-bar navbar-kebab"></span>
      </button>

      <collapse-transition>
        <div class="collapse navbar-collapse show" v-show="showMenu">
          <ul class="navbar-nav" :class="$rtl.isRTL ? 'mr-auto' : 'ml-auto'">
            <div
              class="search-bar input-group"
              @click="searchModalVisible = true"
            >
              <!-- <input type="text" class="form-control" placeholder="Search...">
              <div class="input-group-addon"><i class="tim-icons icon-zoom-split"></i></div> -->
              <button
                class="btn btn-link"
                id="search-button"
                data-toggle="modal"
                data-target="#searchModal"
                @click="searchModalVisible = true"
              >
                <i class="tim-icons icon-zoom-split"></i>
              </button>
            
            </div>
              <modal
              :show.sync="searchModalVisible"
              class="modal-search"
              id="searchModal"
              :centered="false"
              :show-close="true"
            >
            
              <input
                slot="header"
                v-model="searchQuery"
                @keyup.enter="searchItems"
                type="text"
                class="form-control"
                id="inlineFormInputGroup"
                
                placeholder="SEARCH"
              />
              
              <ul class="result-list">
                <li v-for="(result, index) in filteredResults" :key="index" @click="selectResult(result)" class="result-item" @mouseover="highlightResult(index)" @mouseleave="unhighlightResult(index)">
                  {{ result.suburb }}
                </li>
              </ul>
            </modal>
            
          </ul>
        </div>

      </collapse-transition>
    </div>
  </nav>
</template>
<script>
import { CollapseTransition } from "vue2-transitions";
import Modal from "@/components/Modal";
import axios from 'axios'

export default {
  name: "NavBar",
  mounted() {
    this.$emit('update-items', {uvi:this.uvRating,location:this.currentLocation.suburb});
    console.log("current location: ",this.currentLocation);
    this.fetchItems();
    this.fetchWeather(this.currentLocation.lat,this.currentLocation.lon);
    // Simulating fetching data or any logic to populate allLocations
  },
  components: {
    CollapseTransition,
    Modal,
  },
  computed: {
    routeName() {
      const { name } = this.$route;
      return this.capitalizeFirstLetter(name);
    },
    isRTL() {
      return this.$rtl.isRTL;
    },
    filteredResults() {
      return this.searchResults.filter(result =>
        result.suburb.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    },

  },
  data() {
    return {
      activeNotifications: false,
      showMenu: false,
      searchModalVisible: false,
      searchQuery: "",
      searchResults: [],  // to hold the filtered results
      allLocations: [], // this should be your array of items to search from
      uvRating: 0,
      currentLocation:{
        id:617,
        suburb:"Clayton",
        lat:-37.940,
        lon:145.100
      },
  };
},

  watch: {
    searchQuery(newQuery) {
      this.filterResults(newQuery);
    },
  },


  methods: {
    capitalizeFirstLetter(string) {
      return string.charAt(0).toUpperCase() + string.slice(1);
    },
    toggleNotificationDropDown() {
      this.activeNotifications = !this.activeNotifications;
    },
    closeDropDown() {
      this.activeNotifications = false;
    },
    toggleSidebar() {
      this.$sidebar.displaySidebar(!this.$sidebar.showSidebar);
    },
    hideSidebar() {
      this.$sidebar.displaySidebar(false);
    },
    toggleMenu() {
      this.showMenu = !this.showMenu;
    },
    highlightResult(index) {
    this.$set(this.searchResults[index], 'hovered', true);
    },
    unhighlightResult(index) {
    this.$set(this.searchResults[index], 'hovered', false);
    },

    filterResults(query) {
      if (!query) {
        this.searchResults = [];
      } else {
        this.searchResults = this.allLocations.filter((item) =>
          item.suburb.toLowerCase().startsWith(query.toLowerCase())
        );
      }
    },

    //Type keywords in the search bar and select the address
    selectResult(result) {
    this.searchQuery = result.suburb;
    this.searchResults = [];
    this.searchItems();
    },
    /*
    searchItems() {
      axios.get(`http://localhost:8080/geo/search?suburb=${this.searchQuery}`)
        .then(response => {
          this.currentLocation = response.data;
          console.log("currentLocation: ", this.currentLocation.suburb);
          this.fetchWeather(this.currentLocation.lat, this.currentLocation.lon).then(() => {
            this.$emit('update-items', { uvi: this.uvRating, location: this.currentLocation.suburb });
      });
        })
        .catch(error => {
          console.error('There was an error fetching the items:', error);
        });
    },
    */
    searchItems() {
  axios.get(`http://localhost:8080/geo/search?suburb=${this.searchQuery}`)
    .then(response => {
      this.currentLocation = response.data;
      // Now we call fetchWeather and wait for it to complete.
      return this.fetchWeather(this.currentLocation.lat, this.currentLocation.lon);
    })
    .then(() => {
      // After fetchWeather completes, emit the update-items event.
      this.$emit('update-items', { uvi: this.uvRating, location: this.currentLocation.suburb });
    })
    .catch(error => {
      console.error('There was an error:', error);
    });
},

    /* This will be used to retrieve all the surburb name from the backend. */
    fetchItems() {
      axios.get('http://localhost:8080/geo')
        .then(response => {
          this.allLocations = response.data;
          //console.log(response.data);
        })
        .catch(error => {
          console.error('There was an error fetching the items:', error);
        });
    },

    fetchWeather(lat, lon) {
      const apiKey = '15cc9b4fa29b5c23c78cbde85c4ec55c';
      const url = `https://api.openweathermap.org/data/3.0/onecall?lat=${lat}&lon=${lon}&appid=${apiKey}`;

      axios.get(url)
        .then(response => {
          this.uvRating = response.data.current.uvi;
          console.log("uv index: ",this.uvRating);
        })
        .catch(error => {
          console.error('There was an error fetching the items:', error);
        });
    }
    
  },
};
</script>

<style>
.search-container {
  position: absolute;

}

.result-item:hover {
  cursor: pointer;
}

.result-list {
  list-style-type: none;
  padding: 0;
}

.result-item {
  color: black;
}

</style>
