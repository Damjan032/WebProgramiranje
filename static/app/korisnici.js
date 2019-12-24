let loginapp = new Vue({
    el:"#korisnici",
    data: {
        korisnici : null,
        korisnikType : null
    },
    mounted () {
        axios.get('/getKorisnici').then(response => {
            this.korisnici = response.data;
        }); 
        axios.get('/getUserType').then(response => {
            this.korisnikType = response.data;
        }); 
    },
    methods:{
        
    }
});



