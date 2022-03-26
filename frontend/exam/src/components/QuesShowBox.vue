<template>
    <div class="quesShowBox">
        <el-descriptions title="题目信息" border :column="4">
            <el-descriptions-item label="题目ID">{{quesInfo.questionId}}</el-descriptions-item>
            <el-descriptions-item label="关联考试">{{quesInfo.questionLink}}</el-descriptions-item>

            <el-descriptions-item label="分值" style='width: 200px;'>{{quesInfo.questionScore}}
            </el-descriptions-item>
            <el-descriptions-item label="题型">{{quesInfo.questionType}}</el-descriptions-item>
        </el-descriptions>

        <el-descriptions title="选项信息" border :column="3" class='uniDescriptions'>
            <el-descriptions-item label="选项1">
                {{quesInfo.questionOpinion1}}
            </el-descriptions-item>
            <el-descriptions-item label="选项2">
                {{quesInfo.questionOpinion2}}
            </el-descriptions-item>
            <el-descriptions-item label="选项3">
                {{quesInfo.questionOpinion3}}
            </el-descriptions-item>
            <el-descriptions-item label="选项4">
                {{quesInfo.questionOpinion4}}
            </el-descriptions-item>
            <el-descriptions-item label="选项5">
                {{quesInfo.questionOpinion5}}
            </el-descriptions-item>
            <el-descriptions-item label="     ">

            </el-descriptions-item>
            <el-descriptions-item label="答案">
                <el-tag size="small">{{quesInfo.questionRightChoice}}</el-tag>
            </el-descriptions-item>

        </el-descriptions>

        <el-descriptions title="题目" border :column="3" class='uniDescriptions'>
        </el-descriptions>

        <div v-html="compileMarkDown(quesInfo.questionText)"></div>

    </div>
</template>

<script>
    import showdown from "showdown";
    var converter = new showdown.Converter();
    converter.setOption("tables", true);

    export default {
        data() {
            return {

            }
        },
        props: {
            quesInfo: Object,
        },
        mounted() {

        },
        methods: {
            compileMarkDown(val) {
                return converter.makeHtml(val);
            },
        }
    }
</script>

<style>
    .uniDescriptions {
        margin-top: 20px;
    }
</style>
