Vue.component("kat",{
    data:function () {
        return{
            kategorije : null,
            korisnikType:null
        }
    },
    mounted:function () {
        axios.get('/vmKategorije').then(response => {
            this.kategorije = response.data;
            console.log(this.kategorije);
        }).catch(error=>{
            new Toast({
                message:error.response.data.ErrorMessage,
                type: 'danger'
            });
        });
        axios.get('/korisnik').then(response => (
            this.korisnikType = response.data.uloga
        )).catch(error=>{
            new Toast({
                message:error.response.data.ErrorMessage,
                type: 'danger'
            });
        });
    },
    methods:{

    },
    template:`
<div>
<div class="page-header">
    <h2>Pregled kategorija</h2>
</div>
<p>
<table class="table">
    <tr>
        <th>
            Ime
        </th>
        <th>
            Broj jezgara
        </th>
        <th>
            RAM (u GB)
        </th>
        <th>
            Broj GPU jezgara
        </th>
    </tr>

    <tr v-for = "kat in kategorije" >
        <td text-align="center">
            <router-link class = "block-link" :to="{name:'detaljiKat', params:{kat:kat, tipKorisnika:korisnikType}}">
                <p>{{kat.ime}}</p>
            </router-link>
        </td>
        <td text-align="center">
            <router-link class = "block-link" :to="{name:'detaljiKat', params:{kat:kat, tipKorisnika:korisnikType}}">
                {{kat.brJezgra}}
            </router-link>
        </td>
        <td text-align="center">
            <router-link class = "block-link" :to="{name:'detaljiKat', params:{kat:kat, tipKorisnika:korisnikType}}">
                {{kat.RAM}}
            </router-link>
        </td>
        <td text-align="center">
            <router-link class = "block-link" :to="{name:'detaljiKat', params:{kat:kat, tipKorisnika:korisnikType}}">
                {{kat.brGPU}}
            </router-link>
        </td>
    </tr>
        
</table>
<router-link v-if = "korisnikType=='SUPER_ADMIN'" to="/dodajKat">
    <button type="button" class="btn btn-success">
        Dodaj kategoriju virtuelne mašine
    </button>
</router-link>
</p>
</div>

    `


});