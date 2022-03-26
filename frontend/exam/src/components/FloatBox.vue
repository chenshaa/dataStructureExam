// 悬浮按钮组件
<template>
    <div style="position:relative;">
        <div class="button-box" v-drag draggable="false">
            <el-card class="box-card">
                <div class="controlPanal">≡≡≡≡</div>
                <Keybord :value="value" @keyChange="keyChange" @submit='numSubmit'></Keybord>
            </el-card>
        </div>
    </div>
</template>

<script>
    import Keybord from '@/components/Keybord.vue'

    export default {
        components: {
            Keybord
        },
        props: ["value"],
        watch: {
            value(newVal) {
                this.values = newVal
            },
        },
        methods: {
            keyChange(val) {
                this.$emit('keyChange', val)
            },
            numSubmit(val) {
                this.$emit('submit', val)
            },
        },
        directives: {
            drag(el) {
                let oDiv = el
                document.onselectstart = function() {
                    return false
                }
                oDiv.onmousedown = function(e) {
                    let disX = e.clientX - oDiv.offsetLeft
                    let disY = e.clientY - oDiv.offsetTop
                    document.onmousemove = function(e) {

                        let l = e.clientX - disX
                        let t = e.clientY - disY

                        oDiv.style.left = l + 'px'
                        oDiv.style.top = t + 'px'
                    }
                    document.onmouseup = function(e) {
                        document.onmousemove = null
                        document.onmouseup = null
                    }

                    return false
                }
            }
        }
    }
</script>

<style scoped>
    .button-box {
        width: 80px;
        border-radius: 50%;
        position: fixed;
        bottom: 80px;
        right: 400px;
        padding: 0;
        cursor: pointer;
        z-index: 888;
    }

    .btn-bg-img {
        width: 80px;
        height: 80px;

        background-size: cover;
    }

    .font-box {
        width: 80px;
        color: #3193ef;
        text-align: center;
    }

    .box-card {

        width: 360px;
        height: 230px;
    }

    /deep/.el-card__body {
        padding: 0;
    }

    .controlPanal {
        width: 360px;
        height: 30px;
        line-height: 30px;
        text-align: center;
        float: left;
        border: 1px solid #f5f5f5;
        box-sizing: border-box;
        color: #b0b0b0;
    }
</style>
