let virtuelneMasineAktivnosti = new Vue({
    el:"#virtuelneMasineAktivnosti",
    data : {
        aktivnosti : [],
        virtuelnaMasina: ""
    },
    mounted () {
            let uri = window.location.search.substring(1);
            let params = new URLSearchParams(uri);
            console.log(params.get("id"));

            axios.get('/virtuelneMasine/'+params.get("id")).then(response => {
                this.virtuelnaMasina = response.data;
                console.log(this.virtuelnaMasina)
                this.aktivnosti = this.virtuelnaMasina.aktivnosti;
                console.log(this.aktivnosti)
            });

     },


    methods : {

    }
});

