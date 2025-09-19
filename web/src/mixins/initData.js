// import TableTitles from "./components/TableTitles";
export default {
    components: {
        // TableTitles,
    },
    data() {
        return {
            page_size: 10,
            page_num: 1,
            total_num: 0,
            list:[]
        }
    },
    mounted() {
    },
    methods: {
        changePage() {
            
        },
        getList() {
            
        },

        clickAddBtn() {
            this.$refs.addForm.dialog = true
        }
    },
}
