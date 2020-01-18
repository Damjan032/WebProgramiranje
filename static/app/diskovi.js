let diskapp = new Vue({
    el:"#diskapp",
    data:{
        tipKorisnika:null,
        diskovi:[],
    },
    mounted:function () {
        axios.get("/diskovi").then(response=>{
            this.diskovi = response.data;
        });
        axios.get('/korisnik').then(response => {
            this.tipKorisnika = response.data.uloga;
        });
    },
    methods:{

    }
});