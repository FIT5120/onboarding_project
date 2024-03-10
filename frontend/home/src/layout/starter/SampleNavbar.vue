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
  
              <div v-for="(result, index) in searchResults" :key="index" @click="searchItems" class="result-item" @mouseover="highlightResult(index)" @mouseleave="unhighlightResult(index)">
                {{ result.suburb }}
              </div>

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
    this.fetchItems();
    this.fetchWeather();
    console.log("allItem:", this.allItems)
    // Simulating fetching data or any logic to populate allItems
    this.$emit('update-items', this.responseData);
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
  },
  data() {
    return {
      activeNotifications: false,
      showMenu: false,
      searchModalVisible: false,
      searchQuery: "",
      searchResults: [],  // to hold the filtered results
      allItems: [], // this should be your array of items to search from
      responseData: 11,
      currentLocation:[]
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
        this.searchResults = this.allItems.filter((item) =>
          item.suburb.toLowerCase().includes(query.toLowerCase())
        );
      }
    },
    // Send the user inputed surbub --> backend use API for the UV Index and return it to responseData
    searchItems(){
      axios.get('http://localhost:8080/geo/search', { params: { suburb: this.searchQuery } })
      .then(response => {
        this.currentLocation = response.data;
        console.log("currentLocation: ",this.currentLocation);
      })
      .catch(error => {
        console.error('There was an error fetching the items:', error);
      });
    },
    /* This will be used to retrieve all the surburb name from the backend. */
    fetchItems() {
      axios.get('http://localhost:8080/geo')
        .then(response => {
          this.allItems = response.data;
          console.log(response.data);
        })
        .catch(error => {
          console.error('There was an error fetching the items:', error);
        });
    },

    fetchWeather() {
      axios.get('https://api.openweathermap.org/data/2.5/onecall?lat=-35.28&lon=149.12&exclude=current&appid=b6c75eaf141a32191c638baa7ad4d720')
        .then(response => {
          this.allItems = response.data;
          console.log("weather: ",response.data);
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


</style>
