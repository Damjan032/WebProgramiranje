let orgPregled = new Vue({
    el:"#orgResursi",
    data: {
        organizacija : "",
        resursi : ""
    },
    mounted () {
        let uri = window.location.search.substring(1);
                let params = new URLSearchParams(uri);


                axios.get('/organizacije/'+params.get("id")).then(response => {
                    this.organizacija = response.data;
                    this.resursi = this.organizacija.resursi;;
                });

    },
    methods:{

        dodajVM:function(){
            window.location.href="/orgVirtuelnaMasinaAdd.html?id="+this.organizacija.id;
        },

         dodajDisk:function(){
             window.location.href="/orgDiskAdd.html?id="+this.organizacija.id;
         }
    }
});