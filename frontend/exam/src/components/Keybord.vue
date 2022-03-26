<template>
    <div>
        <div class="keysBox">
            <div v-for="(m,index) in keys" @click="clickKey(m,index)"
                :class="(index==keyIndex?'active':'' )+ (m==='←' || m=='确认'?'bgHs':'')">
                {{m}}
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                keys: [1, 2, 3, 4, 5, 6, 7, 8, 9, "←", 0, '确认'],
                keyIndex: -1,
                values: '',
            }
        },
        props: ["value"],
        watch: {
            value(newVal) {
                this.values = newVal
            },
        },
        mounted() {
            this.values = this.value;
        },
        methods: {
            clickKey(m, ind) {
                if (m == undefined) {
                    return
                } else {
                    if (m == '←') {
                        this.clickStyle(ind)
                        this.values = this.values.substr(0, this.values.length - 1)
                        this.$emit('keyChange', this.values)
                    } else if (m == '确认') {
                        this.clickStyle(ind)
                        this.$emit('submit', this.values)
                    } else {
                        this.clickStyle(ind)
                        this.values = this.values + m
                        this.$emit('keyChange', this.values)
                    }
                }
            },
            clickStyle(ind) {
                this.keyIndex = ind;
                setTimeout(() => {
                    this.keyIndex = -1
                }, 300)

            }

        }
    }
</script>

<style>
    .keysBox {
        width: 360px;
        height: 200px;
        max-width: 360px;
        background-color: white;
    }

    .keysBox>div {
        width: 33.33%;
        height: 50px;
        line-height: 50px;
        text-align: center;
        float: left;
        border: 1px solid #f5f5f5;
        box-sizing: border-box;
    }

    .keysBox .active {
        background: #eee;
        color: #333333;
    }

    .bgHs {
        background: #eee;

    }
</style>
