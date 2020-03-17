Vue.component("pregled-org",{
    data:function () {
        return{                
            organizacije : null,
            korisnikType : null
        }
    },
    mounted:function () {
        axios.get('/organizacije').then(response => {
            this.organizacije = response.data;
        }).catch(error=> {
            let msg = error.response.data.ErrorMessage;
            new Toast({
                message: msg,
                type: 'danger'
            });
        }); 
        axios.get('/korisnik').then(response => {
            if(response.data==null){
                window.location.replace("/");
            }
            this.korisnikType = response.data.uloga;
        }).catch(error=> {
            let msg = error.response.data.ErrorMessage;
            new Toast({
                message: msg,
                type: 'danger'
            });
        });  
    },
    methods:{
        toDetaljiOrg:function(org) {
            this.$router.push({name:"detaljiOrg", params:{org:org, tipKorisnika:this.korisnikType}})
        },
    },
    template:`
<div>
    <v-card>
        <v-container>
            <h2>Pregled organizacija</h2>
            <p>
                <h4 class="error my-10" v-if="organizacije&&organizacije.length==0">
                Trenutno nema organizacija za pregled.
                </h4>
                <v-simple-table v-else>
                    <thead>
                        <th>
                            Logo
                        </th>
                        <th>
                            Naziv
                        </th>
                        <th>
                            Opis
                        </th>
                    </thead>
                    <tbody>
                        <tr v-for = "o in organizacije" @click="toDetaljiOrg(o)">
                            <td>
                                <img width="50" height="50" v-bind:src="o.imgPath" class="rounded" >
                            </td>
                            <td text-align="center">
                                {{o.ime}}
                            </td>
                            <td text-align="center">
                                {{o.opis}}
                            </td>
                        </tr>
                    </tbody>
                </v-simple-table>
            </p>
            <v-card-actions>
                <router-link v-if = "korisnikType=='SUPER_ADMIN'" to="/dodajOrg">
                    <v-btn color="success">Dodaj organizaciju</v-btn>
                </router-link>
            </v-card-actions>
        </v-container>
    </v-card>
</div>
    `


});

