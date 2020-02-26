Vue.use(VueRouter);
// const header = { template: '<site-header></site-header>' }

const prijava = { template: '<prijava></prijava>' }

const org = { template: '<org></org>' }
const pregledOrg = { template: '<pregled-org></pregled-org>' }
const detaljiOrg = { template: '<detalji-org></detalji-org>' }
const dodajOrg = { template: '<dodaj-org></dodaj-org>' }

const diskovi = { template: '<diskovi></diskovi>' }
const pregledDiskova = { template: '<pregled-disk></pregled-disk>' }
const detaljiDiska = { template: '<detalji-diska></detalji-diska>' }
const dodajDIsk = { template: '<dodaj-disk></dodaj-disk>' }

const korisnici = { template: '<korisnici></korisnici>' };
const pregledKorisnika = { template: '<pregled-korisnika></pregled-korisnika>' };
const detaljiKorisnika = { template: '<detalji-korisnika></detalji-korisnika>' };
const dodajKorisnika = {template:"<dodaj-korisnika></dodaj-korisnika>"};

const vm = {template:'<vm></vm>'};
const pregledVM = { template: '<pregled-vm></pregled-vm>' }
const detaljiVM = { template: '<detalji-vm></detalji-vm>' }
const dodajVM = { template: '<dodaj-vm></dodaj-vm>' }
const virtuelneMasineAktinvosti  = { template: '<vm-aktivnosti></vm-aktivnosti>' }


const kat = { template: '<kat></kat>' }
const pregledKat = { template: '<pregled-kat></pregled-kat>' }
const detaljiKat = { template: '<detalji-kat></detalji-kat>' }
const dodajKat = { template: '<dodaj-kat></dodaj-kat>' }

const router = new VueRouter({
    routes: [
        {
            path:"/",
            name:"Index",
            component:prijava
        },
        {
            path:"/org",
            component: org,
            children:
            [
                {
                    path:'/',
                    component:pregledOrg
                },
                { path: '/dodajOrg',
                    component: dodajOrg
                },  
                { path: '/detaljiOrg/:org/:tipKorisnika',
                    name:"detaljiOrg",
                    component: detaljiOrg,
                    props: true 
                }
            ]
        },
        { path: '/disk',
            component: diskovi,
            children:[
                {
                    path:'/',
                    component:pregledDiskova
                },
                { path: '/dodajDisk',
                    component: dodajDIsk
                },  
                { path: '/detaljiDiska/:disk/:tipKorisnika',
                    name:"detaljiDiska",
                    component: detaljiDiska,
                    props: true 
                }
            ]
        },
        { 
            path: '/korisnik',
            component: korisnici,
            children:[
                {
                    path:'/',
                    component:pregledKorisnika
                },
                { path: '/detaljiKorisnika/:korisnik',
                    name:"detaljiKorisnika",
                    component: detaljiKorisnika,
                    props: true 
                },
                { path: '/dodajKorisnika',
                    component: dodajKorisnika,
                }          
            ]
        },
        { path: '/kat',
            component: kat,
            children:[
                {
                    path:'/',
                    component:pregledKat
                },
                { path: '/dodajKat',
                    component: dodajKat
                },  
                { path: '/detaljiKat/:kat/:tipKorisnika',
                    name:"detaljiKat",
                    component: detaljiKat,
                    props: true 
                }
            ]
        }
    ,
        { 
            path: '/vm',
            component: vm,
            children:[
                {
                    path:'/',
                    component: pregledVM
                },
                { path: '/dodajVM',
                    component: dodajVM
                },
                { path: '/aktinvosti/:id',
                    component: virtuelneMasineAktinvosti
                },
                { path: '/detaljiVM/:vm/:tipKorisnika',
                    name:"detaljiVM",
                    component: detaljiVM,
                    props: true
                }
            ]
        },
        
    ]
});

new Vue({
    router,
    el:"#app",
    template:`
<div>
    <site-header/>
    <router-view></router-view>
 </div>
`
});



