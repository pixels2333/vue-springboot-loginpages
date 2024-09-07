<template>
    <div style="width: 100vw;height: 100vh;overflow: hidden;display: flex;">
        <div style="flex: 1;background-color:dodgerblue;">
            <el-image style="width: 100%;height: 100%;" fit="cover"
                src="https://pic.netbian.com/uploads/allimg/240901/091013-172515301343e0.jpg" />
        </div>
        <div class="website-title">
            <div style="font-size: 30px;font-weight: bold;">欢迎来到我们的学习平台</div>
            <div style="margin-top: 10px;">在这里你可以学习如何使用java,搭建网站,并且与java之父密切交流</div>
            <div style="margin-top: 5px;">在这里你可以同性交友,因为都是男的没有学java的女生</div>

        </div>
        <div style="width: 400px;background-color: white;">
            <router-view />
        </div>
    </div>
</template>
<script setup>


</script>
<style scoped>
.website-title{
    position: absolute;
    bottom: 30px;
    left: 30px;
    color: white;
    text-shadow: 0 0 10px black;

}
</style>