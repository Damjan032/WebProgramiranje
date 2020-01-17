let orgPregled = new Vue({
    el:"#organizacije",
    data: {
        organizacije : null,
        korisnikType : null
    },
    mounted () {
        axios.get('/organizacije').then(response => {
            this.organizacije = response.data;
            console.log(this.organizacije);
        });
        axios.get('/korisnik').then(response => {
            this.korisnikType = response.data.uloga;
        });
    },
    methods:{
        obrisi:function(id){
            axios.delete('/organizacije/'+id).then(response => {
                      window.location.reload();
            });
        },

        pregledKorisnika:function(id){
            window.location.href="/orgKorisnici.html?id="+id;
        },

        izmeni:function(id){
            window.location.href="/orgIzmena.html?id="+id;
        },

        pregledResursa:function(id){
            window.location.href="/orgResursi.html?id="+id;
        }
    }
});