Vue.component("v-date-time",{
    data:function () {
        return{
            date : "",
            time:"",
            datetime:""
        }
    },
    props:[
        'value'
    ],
    watch:{
        value:function (n) {
            this.datetime = n;
        }
    },
    mounted:function () {
        let i =  this.value.indexOf(" ");
        this.date = this.value.slice(0, i);
        this.time = this.value.slice(i+1, this.value.length);
    },
    methods:{
        updateDate:function () {
            if(this.date && this.time){
                this.$emit('input', this.date+" "+ this.time);
            }
        }
        
    },
    template:`
<div>
    <v-row>
        <v-date-picker v-model="date" show-current="2013-07-13" @input="updateDate()"></v-date-picker>
        <v-layout justify-center fill-height>
            <v-divider vertical></v-divider>
        </v-layout>
        <v-time-picker v-model="time" format="24hr" @input="updateDate()"></v-time-picker>
    </v-row>
</div>

    `


});