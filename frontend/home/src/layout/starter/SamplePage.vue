<template>
  <div class="page-container">
    <NavBar @update-items="updateItems"></NavBar>
    <div class="boxes">
      <div class="box box1">
        <div class="box-content">
          <p class= "uvnum">{{ uvRating }}</p>
          
        <p class="uvlevel-msg" :style="{ color: uvLevelColor }">{{ uvLevelMessage }}</p>
        </div>

      </div>
      <div class="box box2">
        <!-- Content for the second box -->
        <p class="message-box">{{ uvMessage }}</p>
      
      </div>
    </div>
  </div>
</template>

<script>
import NavBar from './SampleNavbar.vue';

export default {
  name: "starter-page",
  components: {
    NavBar
  },
  data() {
    return {
      allItems: [],
      uvRating: 7,
    };
  },
  methods: {
    updateItems(items) {
      this.uvRating= items;
    }
  },
  computed:{
    uvMessage() {
      if (this.uvRating >= 1 && this.uvRating <= 2) {
        return "Wear sunglasses, sunscreen, a hat if you plan on staying out for a prolonged period.";
      } else if (this.uvRating >= 3 && this.uvRating <= 5) {
        return "Wear sunglasses, sunscreen, a hat, and protective clothing if you plan on staying out for a prolonged period.";
      } else if (this.uvRating >= 6 && this.uvRating <= 7) {
        return "Wear sunglasses, sunscreen, a hat, protective clothing, and try to reduce time in the sun if you plan on staying out for a prolonged period.";
      } else if (this.uvRating >= 8) {
        return "Try to stay indoors.";
      }
      return "Check UV rating."; // Default message if UV rating is out of expected range
    },

    uvLevelMessage() {
      if (this.uvRating >= 1 && this.uvRating <= 2) {
        return "Low";
      } else if (this.uvRating >= 3 && this.uvRating <= 5) {
        return "Moderate";
      } else if (this.uvRating >= 6 && this.uvRating <= 7) {
        return "High";
      } else if (this.uvRating >= 8 && this.uvRating <= 10) {
        return "Very High";
      } else if (this.uvRating >= 11) {
        return "Extreme";
      }
      return "Invalid UV Rating"; // Default message
    },
    uvLevelColor() {
    if (this.uvRating >= 1 && this.uvRating <= 2) {
      return 'lightseagreen';
    } else if (this.uvRating >= 3 && this.uvRating <= 5) {
      return 'lightgreen';
    } else if (this.uvRating >= 6 && this.uvRating <= 7) {
      return 'yellow';
    } else if (this.uvRating >= 8 && this.uvRating <= 10) {
      return 'orange';
    } else if (this.uvRating >= 11) {
      return 'red';
    }
    return 'black'; // Default color
  }
  },
  
  
};
</script>

<style>
.page-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 80vh;
  margin-right: 85px;
}

.boxes {
  display: flex;
  justify-content: center;
}

.box {
  
  display: flex;
  align-items: center; /* Vertically center the content */
  justify-content: center; /* Horizontally center the content */
  margin: 10px;
  padding: 20px;
  border: 1px solid white;
  width: 550px;
  height: 550px;
  border-radius: 20px;
  text-align: center; /* Ensure text is centered */
}

.box-content {
display: flex;
flex-direction: column; /* Stack children vertically */
  align-items: center; /* Center children horizontally */
}
.message-box{
  font-size: 20px; /* Set font size to 20px */
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.uvlevel-msg{
  margin: 10px;
  font-size: 60px; 


}

.uvnum{
  font-size: 100px; /* Set font size to 20px */
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  color: lightskyblue;
}
</style>
