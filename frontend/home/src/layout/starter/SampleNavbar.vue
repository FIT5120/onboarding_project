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
  
              <div v-for="(result, index) in searchResults" :key="index">
                {{ result }}
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

export default {
  name: "NavBar",
  mounted() {
    // Simulating fetching data or any logic to populate allItems
    this.$emit('update-items', this.allItems);
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
  provide() {
    return {
      allItemsProvided: () => this.allItems,
    };
  },
  data() {
    return {
      activeNotifications: false,
      showMenu: false,
      searchModalVisible: false,
      searchQuery: "",
      searchQuery: "",
      searchResults: [],  // to hold the filtered results
      allItems: ['Alice', 'Bob', 'Charlie', 'David', 'EvA', 'Frank'], // this should be your array of items to search from
      responseData: null,
  };
},

  watch: {
    searchQuery(newQuery) {
      this.filterResults(newQuery);
    },

    allItems(newVal) {
      this.$emit('update-items', newVal);
    }
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
    filterResults(query) {
      if (!query) {
        this.searchResults = [];
      } else {
        this.searchResults = this.allItems.filter((item) =>
          item.toLowerCase().includes(query.toLowerCase())
        );
      }
    },
    /* Send the user inputed surbub --> backend use API for the UV Index and return it to responseData
    searchItems(){
      /*
      axios.get('/api/items/search', { params: { query: this.searchQuery } })
      .then(response => {
        this.responseData = response.data;
      })
      .catch(error => {
        console.error('There was an error fetching the items:', error);
      });
    },*/
    /* This will be used to retrieve all the surburb name from the backend. */
    fetchItems() {
      axios.get('/api/items')
        .then(response => {
          this.allItems = response.data;
        })
        .catch(error => {
          console.error('There was an error fetching the items:', error);
        });
    },
    
  },
};


</script>
<style>
.search-container {
  position: absolute;

}

</style>