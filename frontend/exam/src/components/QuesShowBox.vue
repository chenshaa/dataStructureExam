<template>
    <div class="quesShowBox">
        <el-descriptions title="题目信息" border :column="4">
            <el-descriptions-item label="题目ID">{{quesInfo.questionId}}</el-descriptions-item>
            <el-descriptions-item label="关联考试">{{quesInfo.questionLink}}</el-descriptions-item>

            <el-descriptions-item label="分值" style='width: 200px;'>{{quesInfo.questionScore}}
            </el-descriptions-item>
            <el-descriptions-item label="题型">
                <el-tag type="success" disable-transitions>
                    {{quesInfo.questionTypeText}}
                </el-tag>

            </el-descriptions-item>
        </el-descriptions>

        <el-descriptions title="选项信息" border :column="3" class='uniDescriptions'>
            <el-descriptions-item label="选项1">
                <el-tag v-if="quesInfo.questionOpinion1!=null" effect="dark"
                    :type="answerDisplayList[1] == true ? 'success' : 'danger'" disable-transitions>
                    {{quesInfo.questionOpinion1}}
                </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="选项2">
                <el-tag v-if="quesInfo.questionOpinion2!=null" effect="dark"
                    :type="answerDisplayList[2] == true ? 'success' : 'danger'" disable-transitions>
                    {{quesInfo.questionOpinion2}}
                </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="选项3">
                <el-tag v-if="quesInfo.questionOpinion3!=null" effect="dark"
                    :type="answerDisplayList[3] == true ? 'success' : 'danger'" disable-transitions>
                    {{quesInfo.questionOpinion3}}
                </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="选项4">
                <el-tag v-if="quesInfo.questionOpinion4!=null" effect="dark"
                    :type="answerDisplayList[4] == true ? 'success' : 'danger'" disable-transitions>
                    {{quesInfo.questionOpinion4}}
                </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="选项5">
                <el-tag v-if="quesInfo.questionOpinion5!=null" effect="dark"
                    :type="answerDisplayList[5] == true ? 'success' : 'danger'" disable-transitions>
                    {{quesInfo.questionOpinion5}}
                </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="     ">

            </el-descriptions-item>
            <el-descriptions-item label="答案">
                <el-tag v-if="tagDisplay" id="answerTag" size="small" type="success" v-for="item in rightChoicesList"
                    effect="dark">
                    {{item}}
                </el-tag>
                <el-tag v-if="!tagDisplay" size="small" type="success" effect="dark">{{quesInfo.questionRightChoice}}
                </el-tag>
            </el-descriptions-item>

        </el-descriptions>

        <el-descriptions title="题目" border :column="3" class='uniDescriptions'>
        </el-descriptions>

        <el-card class="box-card" shadow="hover">
            <div v-html="compileMarkDown(quesInfo.questionText)"></div>
        </el-card>


    </div>
</template>

<script>
    import showdown from "showdown";
    var converter = new showdown.Converter();
    converter.setOption("tables", true);

    export default {
        data() {
            return {
                rightChoicesList: [],
                tagDisplay: false,
                answerDisplayList: [],
                quesType: '',
                quesTypeItems: [{
                        name: '单选题',
                        value: '0'
                    },
                    {
                        name: '多选题',
                        value: '1'
                    },
                    {
                        name: '不定项选择题',
                        value: '2'
                    },
                    {
                        name: '填空题',
                        value: '3'
                    },
                    {
                        name: '判断题',
                        value: '4'
                    },
                    {
                        name: '简答题',
                        value: '5'
                    },
                    {
                        name: '综合题',
                        value: '6'
                    }
                ],
            }
        },
        props: {
            quesInfo: Object,
        },
        created() {
            this.load();
        },
        methods: {
            compileMarkDown(val) {
                return converter.makeHtml(val);
            },
            load() {
                console.log(this.quesInfo.questionType)
                this.tagDisplay = this.quesInfo.questionType <= 3;
                if (this.tagDisplay) {
                    this.rightChoicesList = this.quesInfo.questionRightChoice.split("-");

                    //正确选项标签
                    for (var i = 1; i <= 5; i++) {
                        var temp = false;
                        for (var j = 0; j < this.rightChoicesList.length; j++) {
                            if (this.rightChoicesList[j] == i.toString()) {
                                temp = true;
                                break;
                            }
                        }
                        this.answerDisplayList[i] = temp;
                    }
                }

                for (var i = 0; i < this.quesTypeItems.length; i++) {
                    if (this.quesTypeItems[i].value == this.quesInfo.questionType) {
                        this.quesInfo.questionTypeText = this.quesTypeItems[i].name;
                    }
                }

            }



        }
    }
</script>

<style>
    .uniDescriptions {
        margin-top: 20px;
    }

    #answerTag {
        margin-right: 10px;
    }
</style>
