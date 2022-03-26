<template>
    <div style="text-align: center">
        <div class="answerSheet" v-if="showSheet">
            <div v-for="(m,index) in data" @click="clickKey(index)" ref="sheetDiv"
                :class="(m.answerSituation=='0'?'answer-None':'')+(m.answerSituation=='1'?'answer-Correct':'')+(m.answerSituation=='2'?'answer-Done':'')">
                {{index}}
            </div>
        </div>
        <div class="answerSelect" v-if="!showSheet">

        </div>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                showSheet: true,
                select: 0,
            }
        },
        props: ["data", 'set'],
        watch: {
            set(newVal) {
                this.clickKey(newVal);
            },
        },
        methods: {
            clickKey(index) {
                this.$emit('keyChange', index);
                this.$refs.sheetDiv[this.select].style.border = '3px solid #f5f5f5';
                this.$refs.sheetDiv[index].style.border = '3px solid #909399';
                this.select = index;
            },
        },
        mounted() {
            this.clickKey(0);
        }
    }
</script>

<style>
    .answerSheet {
        border: 1px solid #f5f5f5;
        display: inline-block;
    }

    .answerSheet>div {
        width: 40px;
        height: 40px;
        line-height: 34px;
        text-align: center;
        float: left;
        border: 3px solid #f5f5f5;
        box-sizing: border-box;
        border-radius: 4px;
        margin: 5px;
        user-select: none;
    }

    .answerSheet::after {
        display: block;
        height: 0;
        content: '';
        clear: both;
    }

    .answer-None {
        background-color: #E6A23C;
    }

    .answer-None:hover {
        background-color: #ebb563;
    }

    .answer-Done {
        background-color: #67C23A;
    }

    .answer-Done:hover {
        background-color: #85ce61;
    }

    .answer-Correct {
        background-color: #409EFF;
    }

    .answer-Correct:hover {
        background-color: #66b1ff;
    }
</style>
