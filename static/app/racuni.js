let racuniapp = new Vue({
    el:"#racuniapp",
    data: {
        racuni : null,
        pocetniDatum : null,
        krajnjiDatum : null,
        intervalniRacun:null

    },
    mounted () {
       axios.get("/racuni").then(res=>{
           this.racuni = res.data;
       }).catch(error=> {
        let msg = error.response.data.ErrorMessage;
        new Toast({
            message: msg,
            type: 'danger'
        });
    });
        

    },
    methods:{
        pretrazi:function () {
            if(!this.pocetniDatum||!this.krajnjiDatum){
                new Toast({
                    message: 'Niste uneli datume',
                    type: 'danger'
                });
                return;
            }
            axios.get("/racuni/"+this.pocetniDatum+"/"+this.krajnjiDatum)
            .then(response=>{
                this.intervalniRacun = response.data;
            })
            .catch(error=> {
                let msg = error.response.data.ErrorMessage;
                new Toast({
                    message: msg,
                    type: 'danger'
                });
            });
        }
    }
});

