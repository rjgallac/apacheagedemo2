import React, {useEffect, useState} from 'react';
type Person = { id: number; name: string };
type Company = { id: number; name: string };

export default function CompanyForm() {
    const [people, setPeople] = useState<Person[]>([]);
    const [newCompany, setNewCompany] = useState<string>('');
    const [selectedPersonId, setSelectedPersonId] = useState<string>('');
    const [companies, setCompanies] = useState<Company[]>([]);
    const [selectedCompanyId, setSelectedCompanyId] = useState<string>('');


    const fetchPeople = () => {
        fetch('/api/person')
        .then(res => res.json())
        .then((json: Person[]) => {
            setPeople(json);
            if (json.length && !selectedPersonId) setSelectedPersonId(String(json[0].id));
        })
        .catch(err => console.error('Error fetching people:', err));
    };

    const fetchCompanies = () => {
        fetch('/api/company')
        .then(res => res.json())
        .then((json: Company[]) => {
            setCompanies(json);
        })
        .catch(err => console.error('Error fetching companies:', err));
    }

    useEffect(() => {
        fetchPeople();
        fetchCompanies();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const addCompany = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const res = await fetch('/api/company', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                },
                body: JSON.stringify({
                    "name":encodeURIComponent(newCompany),
                })
            });
            const text = await res.text();
            console.log('create-company:', text);
            setNewCompany('');
            //   fetchGraph();
            fetchCompanies();
            
        } catch (err) {
            console.error('Error creating company:', err);
        }
    }

    const linkCompany = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!selectedPersonId || !selectedCompanyId) return;
        try {
            const res = await fetch('/api/company/create-edge', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                },
                body: JSON.stringify({
                    "personId": selectedPersonId,
                    "companyId": selectedCompanyId
                })
            });
            const text = await res.text();
            console.log('create-edge:', text);
            setNewCompany('');
            //   fetchGraph();
            fetchCompanies();
        } catch (err) {
            console.error('Error creating edge:', err);
        }
    }

    const deleteCompany = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!selectedCompanyId) return;
        try {
            const res = await fetch(`/api/company/${selectedCompanyId}`, {
                method: 'DELETE',
            });
            const text = await res.text();
            console.log('delete-company:', text);
            setNewCompany('');
            //   fetchGraph();
            fetchCompanies();
        } catch (err) {
            console.error('Error deleting company:', err);
        }
    }

    return (
        <div>
            <details style={{ padding: 12, backgroundColor: 'cadetblue', opacity: '70%', paddingLeft: '20px', cursor: 'pointer'}}>
                <summary>add company</summary>
                 <form onSubmit={addCompany} >
                    <label>
                        Company Name:
                        <input value={newCompany} onChange={e => setNewCompany(e.target.value)}type="text" style={{width: 'stretch', padding: '10px', marginTop: '10px'}}/>
                    </label>
                    <button type="submit" style={{width: 'stretch', backgroundColor: 'seagreen', borderRadius: '5px', padding: '10px', marginTop: '10px'}}>Create Company</button>
                </form>
            </details>

            <details style={{padding: 12, backgroundColor: 'cadetblue', opacity: '70%', paddingLeft: '20px'}}>
                <summary>link company to person</summary>
                <form onSubmit={linkCompany}>
                    <label style={{display: 'flex', gap: 6, alignItems: 'center'}}>
                        Select person:
                        <select value={selectedPersonId} onChange={e => setSelectedPersonId(e.target.value)} style={{width: 'stretch', padding: '10px', marginTop: '10px'}}>
                            {people.map(p => (
                            <option key={p.id} value={String(p.id)}>{p.name}</option>
                            ))}
                        </select>
                    </label>
                    <label >
                        Select company:
                        <select value={selectedCompanyId} onChange={e => setSelectedCompanyId(e.target.value)} style={{width: 'stretch', padding: '10px', marginTop: '10px'}}>
                            {companies.map(c => (
                            <option key={c.id} value={String(c.id)}>{c.name}</option>
                            ))}
                        </select>
                    </label>
                    <button type="submit" style={{width: 'stretch', backgroundColor: 'seagreen', borderRadius: '5px', padding: '10px', marginTop: '10px'}}>Create Employment Relation</button>
                </form>
            </details>
            <details style={{marginBottom: 12, padding: 12, backgroundColor: 'cadetblue', opacity: '70%', paddingLeft: '20px'}}>
                <summary>Delete Company</summary>
                <form onSubmit={deleteCompany}>
                    <label >
                        Select company:
                        <select value={selectedCompanyId} onChange={e => setSelectedCompanyId(e.target.value)} style={{width: 'stretch', padding: '10px', marginTop: '10px'}}>
                            {companies.map(c => (
                            <option key={c.id} value={String(c.id)}>{c.name}</option>
                            ))}
                        </select>
                    </label>
                    <button type="submit" style={{width: 'stretch', backgroundColor: 'indianred', borderRadius: '5px', padding: '10px', marginTop: '10px'}}>Delete Company</button>
                </form>
            </details>
        </div>
    )
}