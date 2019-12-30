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

    }
});