<script setup>

import TopWitge from "./V3TopWitge.vue";
import { useRoute, useRouter } from 'vue-router'
const router = useRouter()


</script>
<template>
  <div>
    <TopWitge />
      <router-view />
  </div>
</template>
<style scoped>

</style>