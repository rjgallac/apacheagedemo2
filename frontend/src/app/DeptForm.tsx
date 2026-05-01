import { useEffect, useState } from "react";

export default function DeptForm({ refresh } :any ) {
    const [selectedDeptId, setSelectedDeptId] = useState<string>('');
    const [depts, setDepts] = useState<any[]>([]);

    const fetchDepts = () => {
        fetch('/api/department')
        .then(res => res.json())
        .then((json: any[]) => {
            setDepts(json);
            if (json.length && !selectedDeptId) setSelectedDeptId(String(json[0].id));
        })
        .catch(err => console.error('Error fetching departments:', err));
    }

    const addDept = async (e: React.FormEvent) => {
        e.preventDefault();
        const formData = new FormData(e.target as HTMLFormElement);
        const deptName = formData.get('deptName') as string;
        if (!deptName) return;
        try {
            const res = await fetch('/api/department', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                },
                body: JSON.stringify({
                    "name": encodeURIComponent(deptName),
                })
            });
            const text = await res.text();
            console.log('create-dept:', text);
            setSelectedDeptId('');
            refresh();
            // fetchDepts();
        } catch (err) {
            console.error('Error creating department:', err);
        }
    }


    const deleteDept = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const res = await fetch(`/api/dept/${selectedDeptId}`, {
                method: 'DELETE',
            });
            const text = await res.text();
            console.log('delete-dept:', text);
            setSelectedDeptId('');
            refresh();
            // fetchDepts();
        } catch (err) {
            console.error('Error deleting department:', err);
        }
    }

    useEffect(() => {
        fetchDepts();
    }, []);

    return (
        <div>
            <h4>Add Department</h4>

            <form onSubmit={addDept} >
                <label>Department Name:
                    <input type="text" name="deptName" />
                </label>
                <button type="submit" style={{width: 'stretch', backgroundColor: 'seagreen', borderRadius: '5px', padding: '10px', marginTop: '10px'}}>Add Department</button>
            </form>

          

            <h4>link department to company</h4>

            <h4>link department to person</h4>

            <h4>delete department</h4>
            <form onSubmit={deleteDept} >
                <label>Select Department:
                    <select value={selectedDeptId} onChange={e => setSelectedDeptId(e.target.value)} style={{width: 'stretch', padding: '10px', marginTop: '10px'}}>
                        {depts.map(dept => (
                            <option key={dept.id} value={dept.id}>{dept.name}</option>
                        ))}
                    </select>
                </label>
                <button type="submit" style={{width: 'stretch', backgroundColor: 'seagreen', borderRadius: '5px', padding: '10px', marginTop: '10px'}}>Delete Department</button>
            </form>
        </div>
    );
}